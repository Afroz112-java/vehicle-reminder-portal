import React from 'react'
import { Navigate, replace } from 'react-router-dom';

const ProtectedRoute = ({children}) => {
    const isAuthenticated = !!localStorage.getItem('token');
   return isAuthenticated ? children : <Navigate to={"/login"} replace/>;
}

export default ProtectedRoute