import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TaskService implements TaskState{

    private static final Path FILE_PATH = Paths.get("tasks.json");

    @Override
    public void criar(String description) {
        List<TaskModel> tasks = carregarTasks();
        int newId = tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).getId() + 1;

        TaskModel novaTask = new TaskModel(newId, description);
        tasks.add(novaTask);

        salvarTasks(tasks);
        System.out.println("Task added successfully (ID: " + newId + ")");
    }

    @Override
    public void editar(int id, String newDescription, State newState) {
        List<TaskModel> tasks = carregarTasks();
        TaskModel task = findById(tasks, id);

        if (task != null) {
            if (newDescription != null) task.setDescription(newDescription);
            if (newState != null) task.setStatus(newState);

            salvarTasks(tasks);
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Task not found (ID: " + id + ")");
        }
    }

    @Override
    public void excluir(int id) {
        List<TaskModel> tasks = carregarTasks();
        boolean removed = tasks.removeIf(t -> t.getId() == id);

        if (removed) {
            salvarTasks(tasks);
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    @Override
    public void listar(String filtro) {
        List<TaskModel> tasks = carregarTasks();

        System.out.printf("%-5s %-15s %-20s %s%n", "ID", "Status", "Created", "Description");
        System.out.println("------------------------------------------------------------");

        for (TaskModel t : tasks) {
            if (filtro == null || filtro.equals("all") || t.getStatus().toString().equals(filtro)) {
                System.out.printf("%-5d %-15s %-20s %s%n",
                        t.getId(), t.getStatus(), t.getCreatedAt(), t.getDescription());
            }
        }
    }


    private TaskModel findById(List<TaskModel> tasks, int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    private void salvarTasks(List<TaskModel> tasks) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            json.append(tasks.get(i).toJson());
            if (i < tasks.size() - 1) json.append(",");
        }
        json.append("]");

        try {
            Files.write(FILE_PATH, json.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    private List<TaskModel> carregarTasks() {
        List<TaskModel> lista = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) return lista;

        try {
            String content = new String(Files.readAllBytes(FILE_PATH)).trim();
            if (content.isEmpty() || content.equals("[]")) return lista;

            content = content.substring(1, content.length() - 1);
            String[] objetos = content.split("(?<=\\}),(?=\\{)");

            for (String objJson : objetos) {
                lista.add(parseJson(objJson));
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo.");
        }
        return lista;
    }

    private TaskModel parseJson(String json) {
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] pairs = json.split(",");

        int id = 0;
        String desc = "", created = "", updated = "";
        State st = State.TODO;

        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            String key = kv[0].trim();
            String val = kv.length > 1 ? kv[1].trim() : "";

            switch (key) {
                case "id": id = Integer.parseInt(val); break;
                case "description": desc = val; break;
                case "status": st = State.fromString(val); break;
                case "createdAt": created = val; break;
                case "updatedAt": updated = val; break;
            }
        }
        return new TaskModel(id, desc, st, created, updated);
    }
}
