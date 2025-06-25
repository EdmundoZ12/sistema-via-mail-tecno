package com.tecnoweb.grupo7sa.command;

import com.tecnoweb.grupo7sa.handle.HandleReporte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandInterpreter {

    private static final Map<String, String[]> COMMANDS = new HashMap<>();

    static {
        // CU1 - Gestión de Usuarios y Participantes (CRUD básico)
        COMMANDS.put("usuario", new String[]{"save", "update", "delete", "findAll", "findById"});
        COMMANDS.put("participante", new String[]{"save", "update", "delete", "findAll", "findById"});
        COMMANDS.put("tipoparticipante", new String[]{"save", "update", "delete", "findAll", "findById"});

        // CU2 - Gestiones (CRUD + gestión actual)
        COMMANDS.put("gestion", new String[]{"save", "update", "delete", "findAll", "findById", "findCurrent"});

        // CU3 - Gestión de Cursos y Precios (CRUD + consultas básicas)
        COMMANDS.put("curso", new String[]{"save", "update", "delete", "findAll", "findById", "findByGestion"});
        COMMANDS.put("precio", new String[]{"save", "update", "delete", "findAll", "findById", "findByCurso"});

        // CU4 - Gestión de Inscripciones (flujo básico)
        COMMANDS.put("preinscripcion", new String[]{"save", "approve", "reject", "findAll", "findById"});
        COMMANDS.put("inscripcion", new String[]{"findAll", "findById", "updateNota"});

        // CU5 - Control de Cursos (CRUD básico)
        COMMANDS.put("tarea", new String[]{"save", "update", "delete", "findAll", "findById"});
        COMMANDS.put("notatarea", new String[]{"save", "update", "delete", "findAll", "findById"});
        COMMANDS.put("asistencia", new String[]{"save", "update", "delete", "findAll", "findById"});

        // CU6 - Gestión de Pagos (CRUD + búsqueda esencial)
        COMMANDS.put("pago", new String[]{"save", "update", "delete", "findAll", "findById", "findByPreinscripcion"});

        // CU7 - Gestión de Certificados (esenciales)
        COMMANDS.put("certificado", new String[]{"save", "generar", "list", "find", "verificar"});

        // CU8 - Reportes y Estadísticas (simplificado)
        COMMANDS.put("reporte", new String[]{"inscripciones", "aprobacion", "participantes", "certificados", "dashboard"});
    }

    public static String interpret(String subject) {
        subject = subject.replaceAll("[^\\p{L}\\p{N}\\s\\(\\),./@_-]", "");
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

        if (!COMMANDS.containsKey(entity)) {
            return "Entidad '" + entity + "' no reconocida. Usa 'help' para ver entidades disponibles.";
        }

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
            case "preinscripcion":
                return executePreinscripcionCommand(command, params);
            case "inscripcion":
                return executeInscripcionCommand(command, params);
            case "tarea":
                return executeTareaCommand(command, params);
            case "notatarea":
                return executeNotaTareaCommand(command, params);
            case "asistencia":
                return executeAsistenciaCommand(command, params);
            case "pago":
                return executePagoCommand(command, params);
            case "certificado": // ⭐ NUEVO CU7
                return executeCertificadoCommand(command, params);
            case "reporte": // ⭐ NUEVO CU8
                return executeReporteCommand(command, params);
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
            case "findAll":
                return HandleUsuario.findAll();
            case "findById":
                return HandleUsuario.findById(params);
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
            case "findAll":
                return HandleParticipante.findAll();
            case "findById":
                return HandleParticipante.findById(params);
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
            case "findAll":
                return HandleTipoParticipante.findAll();
            case "findById":
                return HandleTipoParticipante.findById(params);
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
            case "findAll":
                return HandleGestion.findAll();
            case "findById":
                return HandleGestion.findById(params);
            case "findCurrent":
                return HandleGestion.findCurrent();
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
            case "findAll":
                return HandleCurso.findAll();
            case "findById":
                return HandleCurso.findById(params);
            case "findByGestion":
                return HandleCurso.findByGestion(params);
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
            case "findAll":
                return HandlePrecio.findAll();
            case "findById":
                return HandlePrecio.findById(params);
            case "findByCurso":
                return HandlePrecio.findByCurso(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

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
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executeInscripcionCommand(String command, String params) {
        switch (command) {
            case "findAll":
                return HandleInscripcion.findAll();
            case "findById":
                return HandleInscripcion.findById(params);
            case "updateNota":
                return HandleInscripcion.updateNota(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executeTareaCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleTarea.save(params);
            case "update":
                return HandleTarea.update(params);
            case "delete":
                return HandleTarea.delete(params);
            case "findAll":
                return HandleTarea.findAll();
            case "findById":
                return HandleTarea.findById(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executeNotaTareaCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleNotaTarea.save(params);
            case "update":
                return HandleNotaTarea.update(params);
            case "delete":
                return HandleNotaTarea.delete(params);
            case "findAll":
                return HandleNotaTarea.findAll();
            case "findById":
                return HandleNotaTarea.findById(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executeAsistenciaCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleAsistencia.save(params);
            case "update":
                return HandleAsistencia.update(params);
            case "delete":
                return HandleAsistencia.delete(params);
            case "findAll":
                return HandleAsistencia.findAll();
            case "findById":
                return HandleAsistencia.findById(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String executePagoCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandlePago.save(params);
            case "update":
                return HandlePago.update(params);
            case "delete":
                return HandlePago.delete(params);
            case "findAll":
                return HandlePago.findAll();
            case "findById":
                return HandlePago.findById(params);
            case "findByPreinscripcion":
                return HandlePago.findByPreinscripcion(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    // ⭐ NUEVO: CU7 - Gestión de Certificados
    private static String executeCertificadoCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleCertificado.save(params);
            case "generar":
                return HandleCertificado.generar(params);
            case "list":
                return HandleCertificado.findAll();
            case "find":
                return HandleCertificado.findOneById(params);
            case "verificar":
                return HandleCertificado.verificar(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    // ⭐ NUEVO: CU8 - Reportes y Estadísticas
    private static String executeReporteCommand(String command, String params) {
        HandleReporte handleReporte = new HandleReporte();

        try {
            switch (command) {
                case "inscripciones":
                    if (params.isEmpty()) {
                        return "Uso: reporte inscripciones (cursoId)";
                    }
                    int cursoId = Integer.parseInt(params.trim());
                    List<String[]> inscripciones = handleReporte.inscripcionesPorCurso(cursoId);
                    if (inscripciones == null || inscripciones.isEmpty()) {
                        return "No hay inscripciones para el curso especificado.";
                    }
                    StringBuilder sbInsc = new StringBuilder("=== INSCRIPCIONES POR CURSO ===\n");
                    for (String[] i : inscripciones) {
                        sbInsc.append("Participante: ").append(i[0]).append(" | Estado: ").append(i[1]).append("\n");
                    }
                    return sbInsc.toString();

                case "aprobacion":
                    if (params.isEmpty()) {
                        return "Uso: reporte aprobacion (cursoId)";
                    }
                    int cursoIdAprob = Integer.parseInt(params.trim());
                    String[] aprobacion = handleReporte.aprobacionPorCurso(cursoIdAprob);
                    if (aprobacion == null) {
                        return "No hay datos de aprobación para el curso especificado.";
                    }
                    return "=== REPORTE DE APROBACIÓN ===\nCurso: " + aprobacion[0] +
                            "\nTotal Inscritos: " + aprobacion[1] +
                            "\nAprobados: " + aprobacion[2] +
                            "\nPorcentaje: " + aprobacion[3] + "%";

                case "participantes":
                    List<String[]> participantes = handleReporte.participantesPorTipo();
                    if (participantes == null || participantes.isEmpty()) {
                        return "No hay datos de participantes por tipo.";
                    }
                    StringBuilder sbPart = new StringBuilder("=== PARTICIPANTES POR TIPO ===\n");
                    for (String[] p : participantes) {
                        sbPart.append("Tipo: ").append(p[0]).append(" | Cantidad: ").append(p[1]).append("\n");
                    }
                    return sbPart.toString();

                case "certificados":
                    if (params.isEmpty()) {
                        return "Uso: reporte certificados (gestionId)";
                    }
                    int gestIdCert = Integer.parseInt(params.trim());
                    List<String[]> certificados = handleReporte.certificadosEmitidos(gestIdCert);
                    if (certificados == null || certificados.isEmpty()) {
                        return "No hay certificados emitidos para la gestión especificada.";
                    }
                    StringBuilder sbCert = new StringBuilder("=== CERTIFICADOS EMITIDOS ===\n");
                    for (String[] c : certificados) {
                        sbCert.append("Participante: ").append(c[0]).append(" | Curso: ").append(c[1]).append(" | Tipo: ").append(c[2]).append("\n");
                    }
                    return sbCert.toString();

                case "dashboard":
                    String[] dashboard = handleReporte.dashboardGeneral();
                    if (dashboard == null) {
                        return "No hay datos para el dashboard general.";
                    }
                    return "=== DASHBOARD GENERAL ===\n" +
                            "Total Participantes: " + dashboard[0] + "\n" +
                            "Total Cursos: " + dashboard[1] + "\n" +
                            "Ingresos del Mes: Bs. " + dashboard[2] + "\n" +
                            "Certificados Emitidos: " + dashboard[3];

                default:
                    return "Comando de reporte no implementado: " + command;
            }
        } catch (NumberFormatException e) {
            return "Error: Parámetro numérico inválido.";
        } catch (Exception e) {
            return "Error al generar reporte: " + e.getMessage();
        } finally {
            handleReporte.disconnect();
        }
    }

    private static String getHelpMessage() {
        return "**************** SISTEMA CICIT - VERSIÓN SIMPLIFICADA ****************\r\n" +
                "\r\n" +
                "🎯 SISTEMA DE GESTIÓN DE CURSOS DE CAPACITACIÓN CICIT\r\n" +
                "📧 Optimizado para funcionamiento por correo electrónico\r\n" +
                "\r\n" +
                "Estructura: {entidad} {comando} (parametros)\r\n" +
                "\r\n" +
                "=== CU1 - USUARIOS Y PARTICIPANTES ===\r\n" +
                "\r\n" +
                "👤 USUARIOS:\r\n" +
                "• save (nombre, apellido, carnet, email, telefono, password, rol, registro)\r\n" +
                "• update (id, nombre, apellido, carnet, email, telefono, password, rol, registro)\r\n" +
                "• delete (id) | findAll () | findById (id)\r\n" +
                "\r\n" +
                "👥 PARTICIPANTES:\r\n" +
                "• save (carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro)\r\n" +
                "• update (id, carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro)\r\n" +
                "• delete (id) | findAll () | findById (id)\r\n" +
                "\r\n" +
                "🏷️ TIPOS DE PARTICIPANTE:\r\n" +
                "• save (codigo, descripcion) | update (id, codigo, descripcion)\r\n" +
                "• delete (id) | findAll () | findById (id)\r\n" +
                "\r\n" +
                "=== CU2 - GESTIONES ACADÉMICAS ===\r\n" +
                "\r\n" +
                "📅 GESTIONES:\r\n" +
                "• save (nombre, descripcion, fechaInicio, fechaFin)\r\n" +
                "• update (id, nombre, descripcion, fechaInicio, fechaFin)\r\n" +
                "• delete (id) | findAll () | findById (id) | findCurrent () ⭐\r\n" +
                "\r\n" +
                "=== CU3 - CURSOS Y PRECIOS ===\r\n" +
                "\r\n" +
                "📚 CURSOS:\r\n" +
                "• save (nombre, descripcion, duracionHoras, nivel, logoUrl, tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin)\r\n" +
                "• update (id, nombre, descripcion, duracionHoras, nivel, logoUrl, tutorId, gestionId, aula, cuposTotales, fechaInicio, fechaFin)\r\n" +
                "• delete (id) | findAll () | findById (id) | findByGestion (gestionId) ⭐\r\n" +
                "\r\n" +
                "💰 PRECIOS:\r\n" +
                "• save (cursoId, tipoParticipanteId, precio) | update (id, cursoId, tipoParticipanteId, precio)\r\n" +
                "• delete (id) | findAll () | findById (id) | findByCurso (cursoId) ⭐\r\n" +
                "\r\n" +
                "=== CU4 - INSCRIPCIONES ===\r\n" +
                "\r\n" +
                "📝 PREINSCRIPCIONES:\r\n" +
                "• save (participanteId, cursoId, observaciones)\r\n" +
                "• approve (preinscripcionId, observaciones) ⭐ CREA INSCRIPCIÓN\r\n" +
                "• reject (preinscripcionId, observaciones)\r\n" +
                "• findAll () | findById (id)\r\n" +
                "\r\n" +
                "✅ INSCRIPCIONES:\r\n" +
                "• findAll () | findById (id)\r\n" +
                "• updateNota (inscripcionId, notaFinal, estado) ⭐ CALIFICAR\r\n" +
                "\r\n" +
                "=== CU5 - CONTROL DE CURSOS ===\r\n" +
                "\r\n" +
                "📋 TAREAS:\r\n" +
                "• save (cursoId, titulo, descripcion, fechaAsignacion)\r\n" +
                "• update (id, cursoId, titulo, descripcion, fechaAsignacion)\r\n" +
                "• delete (id) | findAll () | findById (id)\r\n" +
                "\r\n" +
                "📊 NOTAS DE TAREAS:\r\n" +
                "• save (tareaId, inscripcionId, nota) | update (id, tareaId, inscripcionId, nota)\r\n" +
                "• delete (id) | findAll () | findById (id)\r\n" +
                "\r\n" +
                "📅 ASISTENCIAS:\r\n" +
                "• save (inscripcionId, fecha, estado) | update (id, inscripcionId, fecha, estado)\r\n" +
                "• delete (id) | findAll () | findById (id)\r\n" +
                "\r\n" +
                "=== CU6 - PAGOS ===\r\n" +
                "\r\n" +
                "💳 PAGOS:\r\n" +
                "• save (preinscripcionId, monto, recibo) | update (id, preinscripcionId, monto, recibo)\r\n" +
                "• delete (id) | findAll () | findById (id)\r\n" +
                "• findByPreinscripcion (preinscripcionId) ⭐ VERIFICAR PAGO\r\n" +
                "\r\n" +
                "=== CU7 - CERTIFICADOS ⭐ NUEVO ===\r\n" +
                "\r\n" +
                "📜 CERTIFICADOS:\r\n" +
                "• save (inscripcionId, tipo, codigoVerificacion, urlPdf)\r\n" +
                "• generar (inscripcionId) ⭐ GENERAR AUTOMÁTICO\r\n" +
                "• list () | find (id)\r\n" +
                "• verificar (codigo) ⭐ VALIDAR CERTIFICADO\r\n" +
                "\r\n" +
                "=== CU8 - REPORTES ⭐ NUEVO ===\r\n" +
                "\r\n" +
                "📊 REPORTES:\r\n" +
                "• inscripciones (cursoId) ⭐ LISTA DE INSCRITOS\r\n" +
                "• aprobacion (cursoId) ⭐ PORCENTAJE DE APROBACIÓN\r\n" +
                "• participantes () ⭐ RESUMEN POR TIPO\r\n" +
                "• certificados (gestionId) ⭐ CERTIFICADOS EMITIDOS\r\n" +
                "• dashboard () ⭐ VISTA GENERAL\r\n" +
                "\r\n" +
                "=== 🔄 FLUJO BÁSICO COMPLETO ===\r\n" +
                "\r\n" +
                "# 1. Setup del sistema\r\n" +
                "tipoparticipante save (EST_FICCT, Estudiante FICCT)\r\n" +
                "usuario save (Carlos, Rodriguez, TUT001, carlos@uagrm.edu.bo, 70234567, pass456, TUTOR, 200967890)\r\n" +
                "gestion save (2025-1, Primer Semestre 2025, 2025-02-01, 2025-06-30)\r\n" +
                "\r\n" +
                "# 2. Crear curso\r\n" +
                "gestion findCurrent ()\r\n" +
                "curso save (Python Basico, Introduccion a Python, 40, Basico, null, 1, 1, LAB-INF-2, 25, 2025-03-01, 2025-04-15)\r\n" +
                "precio save (1, 1, 50.00)\r\n" +
                "\r\n" +
                "# 3. Proceso de inscripción\r\n" +
                "participante save (CI12345, Juan, Perez, juan@uagrm.edu.bo, 70987654, UAGRM, 1, 201967890)\r\n" +
                "preinscripcion save (1, 1, Solicitud de inscripcion)\r\n" +
                "preinscripcion findAll ()\r\n" +
                "preinscripcion approve (1, Aprobado)\r\n" +
                "\r\n" +
                "# 4. Control del curso\r\n" +
                "tarea save (1, Variables y Tipos, Ejercicios basicos, 2025-03-05)\r\n" +
                "notatarea save (1, 1, 85.5)\r\n" +
                "asistencia save (1, 2025-03-01, presente)\r\n" +
                "\r\n" +
                "# 5. Registro de pago\r\n" +
                "pago save (1, 50.00, REC-001)\r\n" +
                "pago findByPreinscripcion (1)\r\n" +
                "\r\n" +
                "# 6. Finalización\r\n" +
                "inscripcion updateNota (1, 88.0, APROBADO)\r\n" +
                "\r\n" +
                "# 7. Certificados ⭐ NUEVO\r\n" +
                "certificado generar (1)\r\n" +
                "certificado list ()\r\n" +
                "certificado verificar (CERT123456)\r\n" +
                "\r\n" +
                "# 8. Reportes ⭐ NUEVO\r\n" +
                "reporte inscripciones (1)\r\n" +
                "reporte aprobacion (1)\r\n" +
                "reporte dashboard ()\r\n" +
                "\r\n" +
                "=== 📋 COMANDOS ESENCIALES POR CASO DE USO ===\r\n" +
                "\r\n" +
                "CU1: usuario findAll () | participante findAll ()\r\n" +
                "CU2: gestion findCurrent ()\r\n" +
                "CU3: curso findByGestion (id) | precio findByCurso (id)\r\n" +
                "CU4: preinscripcion findAll () | preinscripcion approve (id)\r\n" +
                "CU5: tarea findAll () | notatarea findAll () | asistencia findAll ()\r\n" +
                "CU6: pago findAll () | pago findByPreinscripcion (id)\r\n" +
                "CU7: certificado generar (id) | certificado verificar (codigo) ⭐\r\n" +
                "CU8: reporte dashboard () | reporte aprobacion (id) ⭐\r\n" +
                "\r\n" +
                "=== 📧 OPTIMIZADO PARA CORREO ELECTRÓNICO ===\r\n" +
                "\r\n" +
                "✅ Solo comandos esenciales (72 vs 107 originales)\r\n" +
                "✅ CRUD básico para todas las entidades\r\n" +
                "✅ Funcionalidades críticas mantenidas\r\n" +
                "✅ Certificados y reportes implementados\r\n" +
                "✅ Sintaxis simple para emails\r\n" +
                "✅ Respuestas concisas\r\n" +
                "\r\n" +
                "=== 📝 NOTAS IMPORTANTES ===\r\n" +
                "\r\n" +
                "• Estados de inscripción: INSCRITO, APROBADO, REPROBADO\r\n" +
                "• Estados de asistencia: presente, ausente, justificado\r\n" +
                "• Tipos de certificado: PARTICIPACION, APROBACION\r\n" +
                "• Niveles de curso: Basico, Intermedio, Avanzado\r\n" +
                "• Roles de usuario: RESPONSABLE, ADMINISTRATIVO, TUTOR\r\n" +
                "• Fechas en formato: YYYY-MM-DD\r\n" +
                "• Use 'null' para campos opcionales\r\n" +
                "\r\n" +
                "🚀 Sistema CICIT - Versión Completa con Certificados y Reportes\r\n" +
                "Desarrollado por Grupo 7SA - Tecnología Web";
    }
}