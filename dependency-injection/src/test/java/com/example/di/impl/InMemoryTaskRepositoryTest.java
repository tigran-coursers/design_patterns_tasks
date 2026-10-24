package com.example.di.impl;

import com.example.di.domain.TaskRepository;

/** The provided implementation passes the contract. PROVIDED CODE - do not change. */
class InMemoryTaskRepositoryTest extends TaskRepositoryContract {

    @Override
    protected TaskRepository createRepository() {
        return new InMemoryTaskRepository();
    }
}
