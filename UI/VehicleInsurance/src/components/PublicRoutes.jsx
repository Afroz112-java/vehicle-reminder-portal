import React from 'react'
import { Navigate } from 'react-router-dom';

const PublicRoutes = ({children}) => {
    const isAuthenticated = !!localStorage.getItem('token');
  return (
    !isAuthenticated ? children : <Navigate to={"/dashboard"} replace/>
  )
}

export default PublicRoutes