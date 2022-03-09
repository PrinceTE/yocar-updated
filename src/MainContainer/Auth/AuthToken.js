let authToken=()=>{
    let tokenCredentials= JSON.parse(localStorage.getItem("jwt"))
   
    return tokenCredentials
}

export default authToken