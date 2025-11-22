import React from 'react'
import Sidebar from './Sidebar'
import TopNavbar from './TopNavbar'

const AppLayout = ({children}) => {
  return (
    <div>
      
        <div className='flex items-start justify-between'>
        <Sidebar/>
        <div className='w-full '>
        <TopNavbar/>
        {children}
        </div>
        </div> 
    </div>
  )
}

export default AppLayout
