package com.tecnoweb.grupo7sa.command;

import java.util.HashMap;
import java.util.Map;

public class CommandInterpreter {

    private static final Map<String, String[]> COMMANDS = new HashMap<>();

    static {
        // CU1 - Gestión de Usuarios y Participantes
        COMMANDS.put("usuario", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByRole", "findByCarnet", "findByEmail"});
        COMMANDS.put("participante", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByCarnet", "findByEmail", "findByTipo", "findByRegistro"});
        COMMANDS.put("tipoparticipante", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByCodigo"});

        // CU2 - Gestiones (Períodos Académicos)
        COMMANDS.put("gestion", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByDateRange", "findCurrent", "findByName"});

        // CU3 - Gestión de Cursos y Precios
        COMMANDS.put("curso", new String[]{"save", "update", "delete", "deletePermanent", "reactivate", "findAll", "findById", "findByGestion", "findByTutor", "findWithSpots", "updateCupos"});
        COMMANDS.put("precio", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByCurso", "findByTipo", "findEspecifico", "updatePrecio"});

        // CU4 - Gestión de Inscripciones ⭐ NUEVO
        COMMANDS.put("preinscripcion", new String[]{"save", "approve", "reject", "findAll", "findById", "findByCurso", "findByParticipante"});
        COMMANDS.put("inscripcion", new String[]{"findAll", "findById", "findByCurso", "findByParticipante", "updateNota", "withdraw", "getEstadisticas"});
    }

    public static String interpret(String subject) {
        subject = subject.replaceAll("[^a-zA-Z0-9\\s\\(\\),./@_-]", "");
        subject = subject.replaceAll("\\s+", " ").trim();

        System.out.println("Subject luego de formatear: " + subject);

        if (subject.equalsIgnoreCase("help")) {
            return getHelpMessage();
        }

        String pattern = "([a-zA-Z]+)\\s+([a-zA-Z]+)\\s*\\((.*)\\)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(subject);

        if (!matcher.matches()) {
            return "Comando no reconocido. Por favor, asegúrate de seguir la estructura: {entidad} {comando} (parametros)";
        }

        String entity = matcher.group(1).trim().toLowerCase();
        String command = matcher.group(2).trim();
        String params = matcher.group(3).trim();

        boolean commandExists = false;
        for (String validCommand : COMMANDS.get(entity)) {
            if (validCommand.equals(command)) {
                commandExists = true;
                break;
            }
        }

        if (!commandExists) {
            return "Comando '" + command + "' no reconocido para '" + entity + "'. Usa 'help' para ver comandos disponibles.";
        }

        // Ejecutar comandos según entidad
        switch (entity) {
            case "usuario":
                return executeUsuarioCommand(command, params);
            case "participante":
                return executeParticipanteCommand(command, params);
            case "tipoparticipante":
                return executeTipoParticipanteCommand(command, params);
            case "gestion":
                return executeGestionCommand(command, params);
            case "curso":
                return executeCursoCommand(command, params);
            case "precio":
                return executePrecioCommand(command, params);
            case "preinscripcion": // ⭐ NUEVO CU4
                return executePreinscripcionCommand(command, params);
            case "inscripcion": // ⭐ NUEVO CU4
                return executeInscripcionCommand(command, params);
            default:
                return "Entidad no implementada aún: " + entity;
        }
    }

    private static String executeUsuarioCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleUsuario.save(params);
            case "update":
                return HandleUsuario.update(params);
            case "delete":
                return HandleUsuario.delete(params);
            case "reactivate":
                return HandleUsuario.reactivate(params);
            case "findAll":
                return HandleUsuario.findAll();
            case "findById":
                return HandleUsuario.findById(params);
            case "findByRole":
                return HandleUsuario.findByRole(params);
            case "findByCarnet":
                return HandleUsuario.findByCarnet(params);
            case "findByEmail":
                return HandleUsuario.findByEmail(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executeParticipanteCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleParticipante.save(params);
            case "update":
                return HandleParticipante.update(params);
            case "delete":
                return HandleParticipante.delete(params);
            case "reactivate":
                return HandleParticipante.reactivate(params);
            case "findAll":
                return HandleParticipante.findAll();
            case "findById":
                return HandleParticipante.findById(params);
            case "findByCarnet":
                return HandleParticipante.findByCarnet(params);
            case "findByEmail":
                return HandleParticipante.findByEmail(params);
            case "findByTipo":
                return HandleParticipante.findByTipo(params);
            case "findByRegistro":
                return HandleParticipante.findByRegistro(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executeTipoParticipanteCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleTipoParticipante.save(params);
            case "update":
                return HandleTipoParticipante.update(params);
            case "delete":
                return HandleTipoParticipante.delete(params);
            case "reactivate":
                return HandleTipoParticipante.reactivate(params);
            case "findAll":
                return HandleTipoParticipante.findAll();
            case "findById":
                return HandleTipoParticipante.findById(params);
            case "findByCodigo":
                return HandleTipoParticipante.findByCodigo(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executeGestionCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleGestion.save(params);
            case "update":
                return HandleGestion.update(params);
            case "delete":
                return HandleGestion.delete(params);
            case "reactivate":
                return HandleGestion.reactivate(params);
            case "findAll":
                return HandleGestion.findAll();
            case "findById":
                return HandleGestion.findById(params);
            case "findByDateRange":
                return HandleGestion.findByDateRange(params);
            case "findCurrent":
                return HandleGestion.findCurrent();
            case "findByName":
                return HandleGestion.findByName(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executeCursoCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleCurso.save(params);
            case "update":
                return HandleCurso.update(params);
            case "delete":
                return HandleCurso.delete(params);
            case "deletePermanent":
                return HandleCurso.deletePermanent(params);
            case "reactivate":
                return HandleCurso.reactivate(params);
            case "findAll":
                return HandleCurso.findAll();
            case "findById":
                return HandleCurso.findById(params);
            case "findByGestion":
                return HandleCurso.findByGestion(params);
            case "findByTutor":
                return HandleCurso.findByTutor(params);
            case "findWithSpots":
                return HandleCurso.findWithSpots();
            case "updateCupos":
                return HandleCurso.updateCupos(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executePrecioCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandlePrecio.save(params);
            case "update":
                return HandlePrecio.update(params);
            case "delete":
                return HandlePrecio.delete(params);
            case "reactivate":
                return HandlePrecio.reactivate(params);
            case "findAll":
                return HandlePrecio.findAll();
            case "findById":
                return HandlePrecio.findById(params);
            case "findByCurso":
                return HandlePrecio.findByCurso(params);
            case "findByTipo":
                return HandlePrecio.findByTipo(params);
            case "findEspecifico":
                return HandlePrecio.findEspecifico(params);
            case "updatePrecio":
                return HandlePrecio.updatePrecio(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    // ⭐ NUEVO: CU4 - Preinscripciones
    private static String executePreinscripcionCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandlePreinscripcion.save(params);
            case "approve":
                return HandlePreinscripcion.approve(params);
            case "reject":
                return HandlePreinscripcion.reject(params);
            case "findAll":
                return HandlePreinscripcion.findAll();
            case "findById":
                return HandlePreinscripcion.findById(params);
            case "findByCurso":
                return HandlePreinscripcion.findByCurso(params);
            case "findByParticipante":
                return HandlePreinscripcion.findByParticipante(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    // ⭐ NUEVO: CU4 - Inscripciones
    private static String executeInscripcionCommand(String command, String params) {
        switch (command) {
            case "findAll":
                return HandleInscripcion.findAll();
            case "findById":
                return HandleInscripcion.findById(params);
            case "findByCurso":
                return HandleInscripcion.findByCurso(params);
            case "findByParticipante":
                return HandleInscripcion.findByParticipante(params);
            case "updateNota":
                return HandleInscripcion.updateNota(params);
            case "withdraw":
                return HandleInscripcion.withdraw(params);
            case "getEstadisticas":
                return HandleInscripcion.getEstadisticas();
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String getHelpMessage() {
        return "**************** SISTEMA CICIT COMPLETO - TODOS LOS CASOS DE USO ****************\r\n" +
                "\r\n" +
                "🎯 SISTEMA DE GESTIÓN DE CURSOS DE CAPACITACIÓN CICIT\r\n" +
                "Usa 'help' para ver la lista de comandos\r\n" +
                "\r\n" +
                "Estructura: {entidad} {comando} (parametros)\r\n" +
                "\r\n" +
                "=== CU1 - USUARIOS ===\r\n" +
                "- save (nombre, apellido, carnet, email, telefono, password, rol, registro)\r\n" +
                "- update (id, nombre, apellido, carnet, email, telefono, password, rol, registro)\r\n" +
                "- delete (id) | reactivate (id)\r\n" +
                "- findAll () | findById (id) | findByRole (TUTOR) | findByCarnet (carnet) | findByEmail (email)\r\n" +
                "\r\n" +
                "=== CU1 - PARTICIPANTES ===\r\n" +
                "- save (carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro)\r\n" +
                "- update (id, carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro)\r\n" +
                "- delete (id) | reactivate (id)\r\n" +
                "- findAll () | findById (id) | findByCarnet (carnet) | findByEmail (email) | findByTipo (id) | findByRegistro (registro)\r\n" +
                "\r\n" +
                "=== CU1 - TIPOS DE PARTICIPANTE ===\r\n" +
                "- save (codigo, descripcion) | update (id, codigo, descripcion) | delete (id) | reactivate (id)\r\n" +
                "- findAll () | findById (id) | findByCodigo (codigo)\r\n" +
                "\r\n" +
                "=== CU2 - GESTIONES (PERÍODOS ACADÉMICOS) ===\r\n" +
                "- save (nombre, descripcion, fechaInicio, fechaFin) | update (id, nombre, descripcion, fechaInicio, fechaFin)\r\n" +
                "- delete (id) | reactivate (id)\r\n" +
                "- findAll () | findById (id) | findByDateRange (fechaInicio, fechaFin) | findCurrent () ⭐ | findByName (nombre)\r\n" +
                "\r\n" +
                "=== CU3 - CURSOS ===\r\n" +
                "- save (nombre, descripcion, duracionHoras, nivel, logoUrl, tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin)\r\n" +
                "- update (id, nombre, descripcion, duracionHoras, nivel, logoUrl, tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin)\r\n" +
                "- delete (id) | deletePermanent (id) ⭐ | reactivate (id)\r\n" +
                "- findAll () ⭐ | findById (id) | findByGestion (gestionId) | findByTutor (tutorId) | findWithSpots () ⭐\r\n" +
                "- updateCupos (cursoId, nuevoCupos) ⭐\r\n" +
                "\r\n" +
                "=== CU3 - PRECIOS ===\r\n" +
                "- save (cursoId, tipoParticipanteId, precio) | update (id, cursoId, tipoParticipanteId, precio)\r\n" +
                "- updatePrecio (id, nuevoPrecio) ⭐ | delete (id) | reactivate (id)\r\n" +
                "- findAll () | findById (id) | findByCurso (cursoId) ⭐ | findByTipo (tipoParticipanteId)\r\n" +
                "- findEspecifico (cursoId, tipoParticipanteId) ⭐\r\n" +
                "\r\n" +
                "=== CU4 - PREINSCRIPCIONES ⭐ NUEVO ===\r\n" +
                "- save (participanteId, cursoId, observaciones)\r\n" +
                "- approve (preinscripcionId, observaciones) ⭐ APRUEBA Y CREA INSCRIPCIÓN\r\n" +
                "- reject (preinscripcionId, observaciones)\r\n" +
                "- findAll () ⭐ PENDIENTES CON PRECIOS\r\n" +
                "- findById (id) | findByCurso (cursoId) | findByParticipante (participanteId)\r\n" +
                "\r\n" +
                "=== CU4 - INSCRIPCIONES ⭐ NUEVO ===\r\n" +
                "- findAll () | findById (id)\r\n" +
                "- findByCurso (cursoId) ⭐ LISTA DE ESTUDIANTES\r\n" +
                "- findByParticipante (participanteId) ⭐ HISTORIAL\r\n" +
                "- updateNota (inscripcionId, notaFinal, estado) ⭐ CALIFICAR\r\n" +
                "- withdraw (inscripcionId, observaciones) ⭐ RETIRAR Y LIBERAR CUPO\r\n" +
                "- getEstadisticas () ⭐ REPORTES POR CURSO\r\n" +
                "\r\n" +
                "=== 🔄 FLUJO COMPLETO DE INSCRIPCIÓN ===\r\n" +
                "\r\n" +
                "# 1. Setup inicial del sistema\r\n" +
                "tipoparticipante save (EST_FICCT, Estudiante FICCT)\r\n" +
                "tipoparticipante save (EST_OTRA, Estudiante otra facultad)\r\n" +
                "tipoparticipante save (PARTICULAR, Particular)\r\n" +
                "\r\n" +
                "usuario save (Maria, Gonzalez, ADM001, maria@uagrm.edu.bo, 70123456, pass123, RESPONSABLE, 201845123)\r\n" +
                "usuario save (Carlos, Rodriguez, TUT001, carlos@uagrm.edu.bo, 70234567, pass456, TUTOR, 200967890)\r\n" +
                "\r\n" +
                "gestion save (2025-1, Primer Semestre 2025, 2025-02-01, 2025-06-30)\r\n" +
                "\r\n" +
                "# 2. Crear curso completo\r\n" +
                "gestion findCurrent ()\r\n" +
                "usuario findByRole (TUTOR)\r\n" +
                "\r\n" +
                "curso save (Python Basico, Introduccion a Python, 40, Basico, null, 2, 1, LAB-INF-2, 25, 2025-03-01, 2025-04-15)\r\n" +
                "\r\n" +
                "precio save (1, 1, 50.00)    # EST_FICCT: $50\r\n" +
                "precio save (1, 2, 80.00)    # EST_OTRA: $80\r\n" +
                "precio save (1, 3, 120.00)   # PARTICULAR: $120\r\n" +
                "\r\n" +
                "curso findWithSpots ()\r\n" +
                "precio findByCurso (1)\r\n" +
                "\r\n" +
                "# 3. Proceso de inscripción completo ⭐\r\n" +
                "participante save (CI12345, Juan, Perez, juan@uagrm.edu.bo, 70987654, UAGRM, 1, 201967890)\r\n" +
                "preinscripcion save (1, 1, Solicitud de inscripcion al curso de Python)\r\n" +
                "\r\n" +
                "# 4. Administrador revisa solicitudes\r\n" +
                "preinscripcion findAll ()    # Ver pendientes con precios\r\n" +
                "\r\n" +
                "# 5. Aprobar preinscripción (crea inscripción automáticamente)\r\n" +
                "preinscripcion approve (1, Aprobado por cumplir requisitos)\r\n" +
                "\r\n" +
                "# 6. Verificar inscripción creada y cupos actualizados\r\n" +
                "inscripcion findByCurso (1)\r\n" +
                "curso findById (1)\r\n" +
                "\r\n" +
                "# 7. Finalizar curso y calificar\r\n" +
                "inscripcion findByCurso (1)               # Ver lista de estudiantes\r\n" +
                "inscripcion updateNota (1, 92.5, APROBADO) # Calificar\r\n" +
                "inscripcion updateNota (2, 68.0, REPROBADO)\r\n" +
                "\r\n" +
                "# 8. Ver estadísticas finales\r\n" +
                "inscripcion getEstadisticas ()            # Reportes completos\r\n" +
                "\r\n" +
                "=== ⭐ FUNCIONALIDADES ESPECIALES ===\r\n" +
                "\r\n" +
                "• preinscripcion approve () - Aprueba y crea inscripción automáticamente\r\n" +
                "• inscripcion withdraw () - Retira estudiante y libera cupo\r\n" +
                "• precio findEspecifico () - Para calcular costo exacto\r\n" +
                "• curso deletePermanent () - Elimina curso y precios (transaccional)\r\n" +
                "• gestion findCurrent () - Solo gestiones vigentes actualmente\r\n" +
                "• inscripcion getEstadisticas () - Reportes completos por curso\r\n" +
                "\r\n" +
                "=== 📋 ENDPOINTS MÁS IMPORTANTES ===\r\n" +
                "\r\n" +
                "CU1: usuario findByRole (TUTOR) | participante findByCarnet (carnet)\r\n" +
                "CU2: gestion findCurrent ()\r\n" +
                "CU3: curso findWithSpots () | precio findByCurso (cursoId)\r\n" +
                "CU4: preinscripcion findAll () | inscripcion findByCurso (cursoId) | inscripcion getEstadisticas ()\r\n" +
                "\r\n" +
                "Notas importantes:\r\n" +
                "- Use 'null' para campos opcionales\r\n" +
                "- Fechas en formato yyyy-MM-dd\r\n" +
                "- Estados válidos: INSCRITO, APROBADO, REPROBADO, RETIRADO\r\n" +
                "- Niveles: Basico, Intermedio, Avanzado\r\n" +
                "- Roles: RESPONSABLE, ADMINISTRATIVO, TUTOR\r\n";
    }
}