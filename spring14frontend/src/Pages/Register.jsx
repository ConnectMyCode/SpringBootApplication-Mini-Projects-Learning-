import { useState } from "react";
import Loading from "../components/Loading.jsx";
import ErrorMessage from "../components/ErrorMessage.jsx";


function Register()
{
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    async function handleSubmit(event){
        event.preventDefault();

        try{
            setLoading(true);
            setError(null);
            setSuccess(false);

            const response = await fetch(
                "http://localhost:8080/api/auth/register",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        username: username,
                        email:email,
                        password:password
                    })
                }
            );

            if(!response.ok){
                throw new Error(`Registration failed: ${response.status}`);
            }

            setSuccess(true);
        }catch(error){setError(error.message)} finally{setLoading(false)}

    }

    return(
        <div  className="register-container">

            <h2>Register</h2>

        <form onSubmit={handleSubmit}>

            <div className="form-group">
                <label>Username</label>
                <input
                    type="text"
                    value={username}
                    onChange={(event) =>
                        setUsername(event.target.value)
                    }
                    required
                />
            </div>

            <div className="form-group">
                <label>Email</label>
                <input type="email"
                        value={email}
                        onChange={(event)=> setEmail(event.target.value) }
                required
                />
            </div>

            <div  className="form-group">
                <label>Password</label>
                <input    type="password"
                          value={password}
                            onChange={(event)=>
                                        setPassword(event.target.value)
                }
                          required
                />
            </div>


            <button type="submit"  disabled={loading}>
                {loading? "Registering..." :"Register"}
            </button>

            {loading && (<Loading message="Registering account..."/>) }

            {error && (<ErrorMessage message={error}/> )}

            {success && (<p>Registration successful! You can now log in.</p>)}
        </form>

        </div>
    );
}
export default Register;