
import './App.css';

import Navbar from './MainContainer/Navbar/Navbar';
import Home from './MainContainer/Home/Home';
import Footer from './MainContainer/Footer/Footer';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { useEffect, useState } from 'react';
import HTTP from './MainContainer/AxiosConfig/Axios';
import Header from './MainContainer/Header/Header';
import Login from './MainContainer/Login/Login';
import authToken from './MainContainer/Auth/AuthToken';
import  axios  from 'axios';



function App() {
  const [cars, setcars] = useState([])
  const [searchTearm, setsearchTearm] = useState("")
  const [searchReasult, setsearchReasult] = useState([])
  let tokenCredentials = authToken()

  const searchHandler = (searchTearm) => {
    setsearchTearm(searchTearm);
    if (searchTearm !== "") {
      const newCarList = cars.filter((car) => {
        return Object.values(car).join(" ").toLowerCase().includes(searchTearm.toLowerCase())
      })
      setsearchReasult(newCarList)
    }
    else {
      setsearchReasult(cars)
    }
  }

  useEffect(() => {

    fetchCar()
  }, [])
  let fetchCar = async () => {

   // let response = await HTTP.get("getAllCars")
   let response=await axios.get("http://localhost:8090/getAllCars")
    //console.log(response.data);

    let fetchCar = response.data.allCars;
    setcars(fetchCar)

      .catch((error) => {
        console.log("Err :", error);
      })
  }

  return (
    <>

      <BrowserRouter>
        <div className="App">
        
          <Navbar />
          <Header term={searchTearm}
            searchKeyword={searchHandler}  />

          <Switch>
            <Route exact path="/"> <Home cars={searchTearm.length < 1 ? cars : searchReasult} 
            fetchCar={fetchCar}
            />
            </Route>
            <Route path="/login"> <Login /></Route>
          </Switch>

          <Footer />
        </div>
      </BrowserRouter>
    </>
  );
}

export default App;


