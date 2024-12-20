import './App.css'
import {ReportTable} from "./components/ReportTable.jsx";
import {ReportSelectedProvider} from "./context/ReportSelectedContext.jsx";

function App() {
    return (
        <div>
            <ReportSelectedProvider>
            <ReportTable />
            </ReportSelectedProvider>
        </div>
    )
}

export default App
