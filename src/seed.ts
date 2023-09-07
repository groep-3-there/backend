import "reflect-metadata"
import { DataSource } from "typeorm"
import { User } from "./entity/User"

//Function to add some dummy data into the database
export async function seed(AppDataSource : DataSource){
    await AppDataSource.manager.save(
        AppDataSource.manager.create(User, {
            id:1,
            firstName: "Eelco",
            lastName: "Jansma",
            age: 27
        })
    )

    await AppDataSource.manager.save(
        AppDataSource.manager.create(User, {
            id: 2,
            firstName: "Jelle",
            lastName: "Blaeser",
            age: 24
        })
    )
}