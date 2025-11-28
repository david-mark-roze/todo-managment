package net.javaguides.todo.service;

import net.javaguides.todo.dto.TodoDTO;

import java.util.Collection;

public interface TodoService {

    TodoDTO create(TodoDTO todoDTO);

    TodoDTO get(Long id);

    Collection<TodoDTO> getAll();

    TodoDTO update(Long id, TodoDTO todoDTO);

    void delete(Long id);

    TodoDTO complete(Long id);

    TodoDTO reopen(Long id);
}
