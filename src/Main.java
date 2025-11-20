

public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("Usage: Main <command> [args]");
            return;
        }

        TaskState service = new TaskService();
        String command = args[0];

        try {
            switch (command) {
                case "add":
                    if (args.length < 2) throw new IllegalArgumentException("Descrição faltante");
                    service.criar(args[1]);
                    break;

                case "update":
                    if (args.length < 3) throw new IllegalArgumentException("ID ou texto faltante");
                    service.editar(Integer.parseInt(args[1]), args[2], null);
                    break;

                case "delete":
                    if (args.length < 2) throw new IllegalArgumentException("ID faltante");
                    service.excluir(Integer.parseInt(args[1]));
                    break;

                case "mark-in-progress":
                    if (args.length < 2) throw new IllegalArgumentException("ID faltante");
                    service.editar(Integer.parseInt(args[1]), null, State.IN_PROGRESS);
                    break;

                case "mark-done":
                    if (args.length < 2) throw new IllegalArgumentException("ID faltante");
                    service.editar(Integer.parseInt(args[1]), null, State.DONE);
                    break;

                case "list":
                    String filter = args.length > 1 ? args[1] : "all";
                    service.listar(filter);
                    break;

                default:
                    System.out.println("Comando não reconhecido.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro: O ID deve ser um número.");
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}