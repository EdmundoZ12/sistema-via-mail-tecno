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

        // Comandos futuros para otros CU (comentados por ahora)
        // COMMANDS.put("gestion", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByDateRange", "findCurrent", "findByName"});
        // COMMANDS.put("curso", new String[]{"save", "update", "delete", "deletePermanent", "reactivate", "findAll", "findById", "findByGestion", "findByTutor", "findWithSpots", "updateCupos"});
        // COMMANDS.put("precio", new String[]{"save", "update", "delete", "reactivate", "findAll", "findById", "findByCurso", "findByTipo", "findEspecifico", "updatePrecio"});
        // COMMANDS.put("preinscripcion", new String[]{"save", "approve", "reject", "findAll", "findById", "findByCurso", "findByParticipante"});
        // COMMANDS.put("inscripcion", new String[]{"findAll", "findById", "findByCurso", "findByParticipante", "updateNota", "withdraw", "getEstadisticas"});
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

        // Ejecutar comandos según entidad
        switch (entity) {
            case "usuario":
                return executeUsuarioCommand(command, params);
            case "participante":
                return executeParticipanteCommand(command, params);
            case "tipoparticipante":
                return executeTipoParticipanteCommand(command, params);
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

    private static String getHelpMessage() {
        return "**************** SISTEMA CICIT - CU1 GESTIÓN DE USUARIOS Y PARTICIPANTES ****************\r\n" +
                "\r\n" +
                "Usa 'help' para ver la lista de comandos\r\n" +
                "\r\n" +
                "Estructura: {entidad} {comando} (parametros)\r\n" +
                "\r\n" +
                "=== CU1 - USUARIOS ===\r\n" +
                "- save (nombre, apellido, carnet, email, telefono, password, rol, registro)\r\n" +
                "- update (id, nombre, apellido, carnet, email, telefono, password, rol, registro)\r\n" +
                "- delete (id)\r\n" +
                "- reactivate (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByRole (RESPONSABLE|ADMINISTRATIVO|TUTOR)\r\n" +
                "- findByCarnet (carnet)\r\n" +
                "- findByEmail (email)\r\n" +
                "\r\n" +
                "=== CU1 - PARTICIPANTES ===\r\n" +
                "- save (carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro)\r\n" +
                "- update (id, carnet, nombre, apellido, email, telefono, universidad, tipoParticipanteId, registro)\r\n" +
                "- delete (id)\r\n" +
                "- reactivate (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByCarnet (carnet)\r\n" +
                "- findByEmail (email)\r\n" +
                "- findByTipo (tipoParticipanteId)\r\n" +
                "- findByRegistro (registro)\r\n" +
                "\r\n" +
                "=== CU1 - TIPOS DE PARTICIPANTE ===\r\n" +
                "- save (codigo, descripcion)\r\n" +
                "- update (id, codigo, descripcion)\r\n" +
                "- delete (id)\r\n" +
                "- reactivate (id)\r\n" +
                "- findAll ()\r\n" +
                "- findById (id)\r\n" +
                "- findByCodigo (codigo)\r\n" +
                "\r\n" +
                "=== EJEMPLOS DE USO CU1 ===\r\n" +
                "\r\n" +
                "Setup inicial:\r\n" +
                "tipoparticipante save (EST_FICCT, Estudiante FICCT)\r\n" +
                "tipoparticipante save (EST_OTRA, Estudiante otra facultad)\r\n" +
                "tipoparticipante save (PARTICULAR, Particular)\r\n" +
                "\r\n" +
                "Crear usuarios:\r\n" +
                "usuario save (Maria, Gonzalez, ADM001, maria@uagrm.edu.bo, 70123456, pass123, RESPONSABLE, 201845123)\r\n" +
                "usuario save (Carlos, Rodriguez, TUT001, carlos@uagrm.edu.bo, 70234567, pass456, TUTOR, 200967890)\r\n" +
                "usuario save (Ana, Lopez, ADM002, ana@uagrm.edu.bo, 70345678, pass789, ADMINISTRATIVO, 201756789)\r\n" +
                "\r\n" +
                "Consultar usuarios:\r\n" +
                "usuario findAll ()\r\n" +
                "usuario findByRole (TUTOR)\r\n" +
                "usuario findByCarnet (ADM001)\r\n" +
                "usuario findByEmail (maria@uagrm.edu.bo)\r\n" +
                "\r\n" +
                "Crear participantes:\r\n" +
                "participante save (CI12345, Juan, Perez, juan@uagrm.edu.bo, 70987654, UAGRM, 1, 201967890)\r\n" +
                "participante save (CI67890, Maria, Silva, maria@gmail.com, null, null, 3, null)\r\n" +
                "participante save (CI11111, Pedro, Mamani, pedro@uagrm.edu.bo, 70111111, UAGRM, 2, 202012345)\r\n" +
                "\r\n" +
                "Consultar participantes:\r\n" +
                "participante findAll ()\r\n" +
                "participante findByCarnet (CI12345)\r\n" +
                "participante findByTipo (1)\r\n" +
                "participante findByRegistro (201967890)\r\n" +
                "participante findByEmail (juan@uagrm.edu.bo)\r\n" +
                "\r\n" +
                "Consultar tipos:\r\n" +
                "tipoparticipante findAll ()\r\n" +
                "tipoparticipante findByCodigo (EST_FICCT)\r\n" +
                "\r\n" +
                "Actualizar y gestionar:\r\n" +
                "usuario update (1, Maria, Gonzalez, ADM001, maria.gonzalez@uagrm.edu.bo, 70123456, newpass, RESPONSABLE, 201845123)\r\n" +
                "participante update (1, CI12345, Juan Carlos, Perez, juan.perez@uagrm.edu.bo, 70987654, UAGRM, 1, 201967890)\r\n" +
                "usuario delete (2)\r\n" +
                "usuario reactivate (2)\r\n" +
                "\r\n" +
                "Nota: Use 'null' para campos opcionales vacíos en participantes (telefono, universidad, registro)\r\n";
    }
}