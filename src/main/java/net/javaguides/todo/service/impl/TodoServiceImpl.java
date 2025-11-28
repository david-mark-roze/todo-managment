package net.javaguides.todo.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.todo.dto.TodoDTO;
import net.javaguides.todo.entity.Todo;
import net.javaguides.todo.exception.ResourceNotFoundException;
import net.javaguides.todo.repository.TodoRepository;
import net.javaguides.todo.service.TodoService;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private TodoRepository repository;
    private ModelMapper modelMapper;

    @Override
    public TodoDTO create(TodoDTO todoDTO) {
        Todo saved = repository.save(modelMapper.map(todoDTO, Todo.class));
        return modelMapper.map(saved, TodoDTO.class);
    }

    @Override
    public TodoDTO get(Long id) {
        return modelMapper.map(findById(id), TodoDTO.class);
    }

    @Override
    public Collection<TodoDTO> getAll() {
        List<Todo> todos = repository.findAll();
        return todos.stream().map(
                (todo -> modelMapper.map(todo, TodoDTO.class))).toList();
    }

    @Override
    public TodoDTO update(Long id, TodoDTO todoDTO) {
        Todo todo = findById(id);
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setComplete(todoDTO.isComplete());
        return modelMapper.map(repository.save(todo), TodoDTO.class);
    }

    @Override
    public void delete(Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return;
        }
        throw new ResourceNotFoundException(String.format("Todo with id %s does not exist", id));
    }

    @Override
    public TodoDTO complete(Long id) {
        Todo todo = findById(id);
        todo.complete();
        return modelMapper.map(repository.save(todo), TodoDTO.class);
    }

    @Override
    public TodoDTO reopen(Long id) {
        Todo todo = findById(id);
        todo.reopen();
        return modelMapper.map(repository.save(todo), TodoDTO.class);
    }


    private Todo findById(Long id){
        return repository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Todo with id %s does not exist", id)));
    }
}
