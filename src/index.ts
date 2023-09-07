import * as express from "express"
import * as bodyParser from "body-parser"
import { Request, Response, Application  } from "express"
import { AppDataSource } from "./data-source"
import { User } from "./entity/User"
import { resolve } from "path"
import {seed} from "./seed"
const usersRouter : express.Router = require("./routes/users")


const PORT = 3000


//Initialize database
AppDataSource.initialize().then(async () => {
    
    const app : Application = express()
    
    
    app.use(bodyParser.json())
    
    
    
    //Routes for /users/...
    app.use("/users", usersRouter)

   
   
    app.get("/seed", async (req : Request, res : Response)=>{
        
        await seed(AppDataSource)
        res.send("done")
    })

    
    
    
    // start express server
    app.listen(PORT)
    console.log(`Server started on port 3000. Open http://localhost:${PORT}/users to see all users, visit /seed to add dummy data into database`)

}).catch(error => console.log(error))
