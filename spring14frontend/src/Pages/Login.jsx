
import {useState} from "react";
import ErrorMessage from "../components/ErrorMessage.jsx";
import Loading from "../components/Loading.jsx";

function Login(props)
{
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    //Handling Success,Loading and Error States.
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

   async function handleSubmit(event){

       try {
           event.preventDefault();
           setLoading(true);
           setError(null);

           const response = await fetch(
               "http://localhost:8080/api/auth/login",
               {
                   method: "POST",
                   headers: {
                       "Content-type": "application/json"
                   },

                   credentials: "include",


                   body: JSON.stringify({
                       username: username,
                       password: password
                   })
               }
           );
           console.log("Login response:", response);

           if (!response.ok) {
               throw new Error(`Login failed: ${response.status}`);
           }


           const data = await response.json();

           console.log("Login data:", data);

           setSuccess(true);

           props.onLoginSuccess();

       }
       catch(error){
           setError(error.message);
           setSuccess(false);
       }
       finally{
            setLoading(false);
       }

   }

    return(
        <div className = "login-container">
            <h2>Login</h2>

            <form onSubmit={handleSubmit}>
                <div className={"form-group"}>
                    <label>Username</label>
                    <input type={"text"}
                            value={username}
                            onChange={(event) => setUsername(event.target.value)}/>
                </div>

                <div className={"form-group"}>
                    <label>Password</label>
                    <input type={"password"}
                        value ={password}
                        onChange={(event)=> setPassword(event.target.value)}/>
                </div>

               <button type={"submit"} disabled={loading}>
                   {loading? "Logging In...":"LogIn"}
               </button>
            </form>
            {loading && <p><Loading message={"Logging In..."}/></p>}

            {error && <p><ErrorMessage message={error}/></p>}

            {success && <p>Login successful!</p>  }

        </div>
    );
}
export default Login;

