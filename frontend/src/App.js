import React from "react";
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import {AuthProvider, useAuth} from './components/AuthContext'
import LoginPage from "./pages/user/Login";
import PrivateRoute from "./components/PrivateRoute";
import HomePage from "./pages/Home"
import AdministrationsPage from "./pages/admin/Administrations";
import ReportsPage from "./pages/admin/Reports";
import UsersPage from "./pages/user/Users";

function App() {

    return (
        <>
            <AuthProvider>
                <Router>
                    <div className="App">
                        <Routes>
                            <Route exact path='/' element={<LoginPage/>}/>
                            <Route exact path='login' element={<LoginPage/>}/>
                            <Route exact path='home' element={<PrivateRoute><HomePage/></PrivateRoute>}/>
                            <Route exact path='administrations'
                                   element={<PrivateRoute><AdministrationsPage/></PrivateRoute>}>
                                <Route exact path='locations' element={<PrivateRoute><h1>Locations</h1></PrivateRoute>}>
                                    <Route exact path='devices'
                                           element={<PrivateRoute><h1>Devices</h1></PrivateRoute>}/>
                                </Route>
                            </Route>
                            <Route exact path='reports' element={<PrivateRoute><ReportsPage/></PrivateRoute>}/>
                            <Route exact path='users' element={<PrivateRoute><UsersPage/></PrivateRoute>}/>
                            <Route path='*' element={<Navigate to="/login"/>}/>
                        </Routes>
                    </div>
                </Router>
            </AuthProvider>

        </>
    );
}

export default App;