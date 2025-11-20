public interface TaskState {
    void criar(String description);
    void editar(int id, String newDescription, State newState);
    void excluir(int id);
    void listar(String filtro);
}
