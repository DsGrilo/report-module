import React, {useState} from "react";


export const ReportSelectedContext = React.createContext();

export const ReportSelectedProvider = ({ children }) => {
    const [selectedValue, setSelectedValue] = useState('');

    const handleSelectChange = (event) => {
        console.log(event.target.value);
        setSelectedValue(event.target.value);
    };

    return (
        <ReportSelectedContext.Provider value={{selectedValue, handleSelectChange}}>{children}</ReportSelectedContext.Provider>
    )
}
