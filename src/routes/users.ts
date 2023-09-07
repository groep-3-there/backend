const express = require('express');
import { AppDataSource } from "../data-source"
import { NextFunction, Request, Response } from "express"
import { User } from "../entity/User"

const usersRouter = express.Router();



usersRouter.get('/', async(req : Request, res : Response) => {
    const userRepository = AppDataSource.getRepository(User)
    const data = await userRepository.find()
    res.send(data)
});

usersRouter.get('/:id', (req, res) => {
    const id = req.params.id
    res.send(id);
});

usersRouter.post('/create', async(req : Request, res : Response) => {
    // Handle POST request for /user
    const userRepository = AppDataSource.getRepository(User)

    const { firstName, lastName, age } = req.body;
    
    const user = Object.assign(new User(), {
        firstName,
        lastName,
        age
    })
    await userRepository.create(user)
    res.send('Create a new user');
});

usersRouter.post('/:id/update', async(req : Request, res : Response) => {
    // Handle POST request for /user
    const userRepository = AppDataSource.getRepository(User)
    const userId = parseInt(req.params.id)
    
    const userToUpdate = await userRepository.findOneBy({
        id: userId,
    })

    const { firstName, lastName, age } = req.body;
    userToUpdate.firstName = firstName
    userToUpdate.lastName = lastName
    userToUpdate.age = age
    await userRepository.save(userToUpdate)
    res.send('Update');
});

usersRouter.get('/:id/remove', async(req : Request, res : Response) => {
    // Handle POST request for /user
    const userRepository = AppDataSource.getRepository(User)
    const userId = parseInt(req.params.id)
    
    const userToRemove = await userRepository.findOneBy({
        id: userId,
    })

    
    await userRepository.remove(userToRemove)
    res.send('Update');
});


module.exports = usersRouter