package com.tecnoweb.grupo7sa.command;

import java.util.HashMap;
import java.util.Map;

public class CommandInterpreter {

    private static final Map<String, String[]> COMMANDS = new HashMap<>();

    static {
        // ⭐ ACTUALIZADO: Añadidos comandos findAllResponsables, findAllAdministrativos, findAllTutores
        COMMANDS.put("usuario", new String[]{"save", "update", "delete", "findAll", "findById", "findByRegister", "findAllResponsables", "findAllAdministrativos", "findAllTutores"});
        COMMANDS.put("participante", new String[]{"save", "update", "delete", "findAll", "findById", "findByCarnet", "findByRegistro", "findByTipo"});
        COMMANDS.put("tipoparticipante", new String[]{"save", "update", "delete", "findAll", "findById", "findByCodigo"});
        
        // 🆕 ACTUALIZADO: Comandos completos para CURSO
        COMMANDS.put("curso", new String[]{"save", "update", "delete", "findAll", "findById", "findByNombre", "findByModalidad", "findByNivel", "findByDuracion"});
    }

    public static String interpret(String subject) {
        subject = subject.replaceAll("[^a-zA-Z0-9\\s\\(\\),./@_-]", "");
        subject = subject.replaceAll("\\s+", " ").trim();

        System.out.println("Subject luego de formatear: " + subject);

        if (subject.equalsIgnoreCase("help")) {
            return getHelpMessage();
        }

        String pattern = "(\\w+)\\s+(\\w+)\\s*\\((.*)\\)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(subject);

        if (!matcher.matches()) {
            return "Comando no reconocido. Por favor, asegúrate de seguir la estructura: {entidad} {comando} (parametros)";
        }

        String entity = matcher.group(1).trim().toLowerCase();
        String command = matcher.group(2).trim();
        String params = matcher.group(3).trim();

        if (!COMMANDS.containsKey(entity)) {
            return "Entidad '" + entity + "' no reconocida. Usa 'help' para ver la lista de entidades y comandos disponibles.";
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

        // Ejecutar comandos
        switch (entity) {
            case "usuario":
                return executeUsuarioCommand(command, params);
            case "participante":
                return executeParticipanteCommand(command, params);
            case "tipoparticipante":
                return executeTipoParticipanteCommand(command, params);
            // 🆕 CURSO: Ejecutor de comandos de CURSO
            case "curso":
                return executeCursoCommand(command, params);
            default:
                return "Entidad no reconocida: " + entity;
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
            case "findByRegister":
                return HandleUsuario.findByRegister(params);
            // ⭐ NUEVOS: Comandos para buscar usuarios por rol
            case "findAllResponsables":
                return HandleUsuario.findAllResponsables();
            case "findAllAdministrativos":
                return HandleUsuario.findAllAdministrativos();
            case "findAllTutores":
                return HandleUsuario.findAllTutores();
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
            case "findByCarnet":
                return HandleParticipante.findByCarnet(params);
            // ⭐ NUEVO: Comando findByRegistro para participante
            case "findByRegistro":
                return HandleParticipante.findByRegistro(params);
            case "findByTipo":
                return HandleParticipante.findByTipo(params);
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
            case "findByCodigo":
                return HandleTipoParticipante.findByCodigo(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    // 🆕 ACTUALIZADO: Ejecutor completo de comandos de CURSO
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
            case "findByNombre":
                return HandleCurso.findByNombre(params);
            case "findByModalidad":
                return HandleCurso.findByModalidad(params);
            case "findByNivel":
                return HandleCurso.findByNivel(params);
            case "findByDuracion":
                return HandleCurso.findByDuracion(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    private static String getHelpMessage() {
        return "**************** SISTEMA PARA CAPACITACIÓN UNIDAD CICIT ****************\r\n" +
                "\r\n" +
                "Usa 'help' para ver la lista de comandos\r\n" +
                "\r\n" +
                "Estructura: {entidad} {comando} (parametros)\r\n" +
                "\r\n" +
                "=== USUARIO ===\r\n" +
                "- save (nombre, apellido, email, registro, telefono, carnet, password, rol)\r\n" +
                "- update (id, nombre, apellido, email, registro, telefono, carnet, password, rol, activo)\r\n" +
                "- delete (id) [SOFT DELETE - Solo desactiva]\r\n" +
                "- findAll () [Solo usuarios activos]\r\n" +
                "- findById (id) [Solo usuarios activos]\r\n" +
                "- findByRegister (registro) [Solo usuarios activos]\r\n" +
                "- findAllResponsables () [Solo responsables activos]\r\n" +
                "- findAllAdministrativos () [Solo administrativos activos]\r\n" +
                "- findAllTutores () [Solo tutores activos]\r\n" +
                "\r\n" +
                "=== PARTICIPANTE ===\r\n" +
                "- save (nombre, apellido, carnet, registro, carrera, email, facultad, telefono, universidad, tipoParticipanteId)\r\n" +
                "- update (id, nombre, apellido, carnet, registro, carrera, email, facultad, telefono, universidad, tipoParticipanteId)\r\n" +
                "- delete (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByCarnet (carnet)\r\n" +
                "- findByRegistro (registro)\r\n" +
                "- findByTipo (tipoParticipanteId)\r\n" +
                "\r\n" +
                "=== TIPO PARTICIPANTE ===\r\n" +
                "- save (nombre, codigo, descripcion)\r\n" +
                "- update (id, nombre, codigo, descripcion)\r\n" +
                "- delete (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByCodigo (codigo)\r\n" +
                "\r\n" +
                "=== CURSO ===\r\n" +
                "- save (nombre, descripcion, logoUrl, duracionHoras, modalidad, nivel, requisitos)\r\n" +
                "- update (id, nombre, descripcion, logoUrl, duracionHoras, modalidad, nivel, requisitos, activo)\r\n" +
                "- delete (id) [SOFT DELETE - Solo desactiva el curso]\r\n" +
                "- findAll () [Solo cursos activos]\r\n" +
                "- findById (id) [Solo cursos activos]\r\n" +
                "- findByNombre (nombre) [Solo cursos activos]\r\n" +
                "- findByModalidad (modalidad: PRESENCIAL/VIRTUAL/HIBRIDO) [Solo cursos activos]\r\n" +
                "- findByNivel (nivel: BASICO/INTERMEDIO/AVANZADO) [Solo cursos activos]\r\n" +
                "- findByDuracion (duracionMinima, duracionMaxima) [Solo cursos activos]\r\n" +
                "\r\n" +
                "=== EJEMPLOS DE USO ===\r\n" +
                "\r\n" +
                "🔹 USUARIO:\r\n" +
                "usuario save (Juan, Perez, juan@email.com, REG001, 70123456, 12345678, password123, RESPONSABLE)\r\n" +
                "usuario update (1, Juan Carlos, Perez Lopez, juan@email.com, REG001, 70123456, 12345678, newpass, TUTOR, true)\r\n" +
                "usuario findAllResponsables ()\r\n" +
                "usuario findAllTutores ()\r\n" +
                "\r\n" +
                "🔹 PARTICIPANTE:\r\n" +
                "participante save (Juan, Perez, CI12345, REG001, Sistemas, juan@email.com, FICCT, 70123456, UAGRM, 1)\r\n" +
                "participante findByCarnet (CI12345)\r\n" +
                "participante findByRegistro (REG001)\r\n" +
                "\r\n" +
                "🔹 TIPO PARTICIPANTE:\r\n" +
                "tipoparticipante save (Estudiante, EST, Estudiante universitario)\r\n" +
                "tipoparticipante findByCodigo (EST)\r\n" +
                "\r\n" +
                "🔹 CURSO:\r\n" +
                "✅ Crear curso:\r\n" +
                "curso save (Java Básico, Curso de programación Java para principiantes, logo.jpg, 40, PRESENCIAL, BASICO, Conocimientos básicos de computación)\r\n" +
                "\r\n" +
                "✅ Actualizar curso (con campo activo):\r\n" +
                "curso update (1, Java Avanzado, Curso avanzado de Java, logo2.jpg, 60, HIBRIDO, AVANZADO, Java intermedio, true)\r\n" +
                "\r\n" +
                "✅ Desactivar curso:\r\n" +
                "curso delete (1)\r\n" +
                "\r\n" +
                "✅ Buscar cursos:\r\n" +
                "curso findByModalidad (VIRTUAL)\r\n" +
                "curso findByNivel (BASICO)\r\n" +
                "curso findByDuracion (20, 50)\r\n" +
                "curso findByNombre (Java Básico)\r\n" +
                "curso findAll ()\r\n" +
                "\r\n" +
                "=== NOTAS IMPORTANTES ===\r\n" +
                "🔸 Todos los métodos de búsqueda solo muestran registros ACTIVOS\r\n" +
                "🔸 Los delete son SOFT DELETE (solo desactiva, no elimina físicamente)\r\n" +
                "🔸 Roles válidos para usuario: RESPONSABLE, ADMINISTRATIVO, TUTOR\r\n" +
                "🔸 Modalidades válidas para curso: PRESENCIAL, VIRTUAL, HIBRIDO\r\n" +
                "🔸 Niveles válidos para curso: BASICO, INTERMEDIO, AVANZADO\r\n" +
                "🔸 Duración de curso: entre 1 y 1000 horas\r\n" +
                "🔸 Campo activo en update: true/false\r\n" +
                "🔸 Validaciones automáticas de formato y longitud en todos los campos\r\n";
    }
}