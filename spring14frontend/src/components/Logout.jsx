import {useState} from "react";
import Loading from "./Loading.jsx";
import ErrorMessage from "./ErrorMessage.jsx";

function Logout(props){

    const [loading,setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    async function handleLogout(){
        try{
            setLoading(true);
            setError(null);
            setSuccess(false);


            const response = await fetch(
                "http://localhost:8080/api/auth/logout",
                {
                    method:"POST",
                    credentials:"include"
                }
            );

            if(!response.ok){
                throw new Error(`Logout failed: ${response.status}`);
            }

            setSuccess(true);
            props.onLogoutSuccess();

        }catch(error){ setError(error.message)}
        finally{ setLoading(false); }
    }

    return(
        <div>
            <h2>Logout</h2>

            <button  onClick ={handleLogout}
                    disabled={loading}>
                {loading? "Logging out...":"Logout"}
            </button>

            {loading && (<Loading message="Logging out..."/>)}

            {error && (<ErrorMessage message={error}/>)}

            {success && (<p>Logout successfull!</p>)}

        </div>
    );



}


export default Logout;