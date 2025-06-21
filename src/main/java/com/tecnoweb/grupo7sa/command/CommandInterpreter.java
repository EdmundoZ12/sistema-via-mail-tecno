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
        // ⭐ NUEVO: Comandos para gestión
        COMMANDS.put("gestion", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByDateRange", "findByName"});
        // ⭐ NUEVO: Comandos para cronograma de curso
        COMMANDS.put("cronogramacurso", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByCursoGestion", "findByFase", "findByFechaRange"});
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
            case "gestion":
                return executeGestionCommand(command, params);
            case "cronogramacurso":
                return executeCronogramaCursoCommand(command, params);
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

    // ⭐ NUEVO: Método para ejecutar comandos de gestión
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
            case "findByName":
                return HandleGestion.findByName(params);
            default:
                return "Comando no implementado: " + command;
        }
    }

    // ⭐ NUEVO: Método para ejecutar comandos de cronograma de curso
    private static String executeCronogramaCursoCommand(String command, String params) {
        switch (command) {
            case "save":
                return HandleCronograma_Curso.save(params);
            case "update":
                return HandleCronograma_Curso.update(params);
            case "delete":
                return HandleCronograma_Curso.delete(params);
            case "reactivate":
                return HandleCronograma_Curso.reactivate(params);
            case "findAll":
                return HandleCronograma_Curso.findAll();
            case "findById":
                return HandleCronograma_Curso.findById(params);
            case "findByCursoGestion":
                return HandleCronograma_Curso.findByCursoGestion(params);
            case "findByFase":
                return HandleCronograma_Curso.findByFase(params);
            case "findByFechaRange":
                return HandleCronograma_Curso.findByFechaRange(params);
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
                "- update (id, nombre, apellido, email, registro, telefono, carnet, password, rol)\r\n" +
                "- delete (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByRegister (registro)\r\n" +
                // ⭐ NUEVOS: Comandos para buscar usuarios por rol
                "- findAllResponsables ()\r\n" +
                "- findAllAdministrativos ()\r\n" +
                "- findAllTutores ()\r\n" +
                "\r\n" +
                "=== PARTICIPANTE ===\r\n" +
                // ⭐ ACTUALIZADO: Nuevo orden de parámetros con campo 'registro'
                "- save (nombre, apellido, carnet, registro, carrera, email, facultad, telefono, universidad, tipoParticipanteId)\r\n" +
                "- update (id, nombre, apellido, carnet, registro, carrera, email, facultad, telefono, universidad, tipoParticipanteId)\r\n" +
                "- delete (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByCarnet (carnet)\r\n" +
                // ⭐ NUEVO: Comando findByRegistro
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
                "=== GESTION ===\r\n" +
                "- save (descripcion, fechaInicio, fechaFin, nombre)\r\n" +
                "- update (id, descripcion, fechaInicio, fechaFin, nombre)\r\n" +
                "- delete (id)\r\n" +
                "- reactivate (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByDateRange (fechaInicio, fechaFin)\r\n" +
                "- findByName (nombre)\r\n" +
                "\r\n" +
                "=== CRONOGRAMA CURSO ===\r\n" +
                "- save (cursoGestionId, descripcion, fase, fechaInicio, fechaFin)\r\n" +
                "- update (id, cursoGestionId, descripcion, fase, fechaInicio, fechaFin)\r\n" +
                "- delete (id)\r\n" +
                "- reactivate (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByCursoGestion (cursoGestionId)\r\n" +
                "- findByFase (fase)\r\n" +
                "- findByFechaRange (fechaInicio, fechaFin)\r\n" +
                "\r\n" +
                "Ejemplos:\r\n" +
                "usuario save (Juan, Perez, juan@email.com, REG001, 70123456, 12345678, password123, RESPONSABLE)\r\n" +
                "usuario findAllResponsables ()\r\n" +
                "usuario findAllTutores ()\r\n" +
                "participante save (Juan, Perez, CI12345, REG001, Sistemas, juan@email.com, FICCT, 70123456, UAGRM, 1)\r\n" +
                "participante findByCarnet (CI12345)\r\n" +
                "participante findByRegistro (REG001)\r\n" +
                "tipoparticipante save (Estudiante, EST, Estudiante universitario)\r\n" +
                "gestion save (Semestre con modalidad híbrida, 2024-03-01, 2024-07-15, Semestre I-2024)\r\n" +
                "gestion update (1, Semestre actualizado, 2024-03-01, 2024-07-20, Semestre I-2024)\r\n" +
                "gestion delete (1)\r\n" +
                "gestion reactivate (1)\r\n" +
                "gestion findAll ()\r\n" +
                "gestion findById (1)\r\n" +
                "gestion findByDateRange (2024-01-01, 2024-12-31)\r\n" +
                "gestion findByName (Semestre)\r\n" +
                "cronogramacurso save (1, Desarrollo de aplicaciones web, planificacion, 2024-03-01, 2024-03-15)\r\n" +
                "cronogramacurso update (1, 1, Desarrollo de aplicaciones web actualizado, desarrollo, 2024-03-01, 2024-03-20)\r\n" +
                "cronogramacurso delete (1)\r\n" +
                "cronogramacurso reactivate (1)\r\n" +
                "cronogramacurso findAll ()\r\n" +
                "cronogramacurso findById (1)\r\n" +
                "cronogramacurso findByCursoGestion (1)\r\n" +
                "cronogramacurso findByFase (planificacion)\r\n" +
                "cronogramacurso findByFechaRange (2024-03-01, 2024-03-31)\r\n";
    }
}