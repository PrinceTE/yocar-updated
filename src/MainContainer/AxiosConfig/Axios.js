import axios from "axios";
import authToken from "../Auth/AuthToken";

let token=authToken()
const HTTP=axios.create({
    baseURL:'http://localhost:8090/' ,
    headers:{
        Authorization:`Bearer ${token}`
    }
})



export default HTTP

