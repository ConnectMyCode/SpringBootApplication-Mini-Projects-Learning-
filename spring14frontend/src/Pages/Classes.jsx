import ErrorMessage from "../components/ErrorMessage.jsx";
import Loading from "../components/Loading.jsx";
import { useState } from "react";
import ClassCard from "../components/ClassCard.jsx";

function Classes(){

    const [hasFetched, setHasFetched] = useState(false);
    const [error,setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [classes, setClasses] = useState([]);

    async function favoriteClass(classId){
        try{ const response= await fetch(`http://localhost:8080/api/classes/${classId}/favorite`,
            {method:"POST",
            credentials:"include"
            });

            if(response.status === 204){

            }

            if(!response.ok){
                throw new Error(`Favorite failed: ${response.status}`);
            }

            console.log(`Favorite failed: ${response.status}`);

            console.log('Class added to Favorites!');
        }
        catch(error){ console.log(error.message);}

    }

    async function bookClass(classId){
        try{
            const response = await fetch(`http://localhost:8080/api/classes/${classId}/book`,
                {method:"POST",
                      credentials:"include"});

            if(response.status === 409){
                alert("You have already Booked this class.")
                return;
            }

            if(!response.ok){ throw new Error(`Booking failed: ${response.status}`);}

            await getClasses();

            console.log("Class Booked successfully!");

        }
        catch(error)
        { console.log(error) }
    }

   async function getClasses() {

       try {
            setLoading(true);
            setError(null);
            setHasFetched(true);

           const response = await fetch(
               "http://localhost:8080/api/classes",
               {credentials: "include"}
           );

           console.log("Status:", response.status);

           console.log("Success:", response.ok);

           if (!response.ok) {
               throw new Error(`HTTP error: ${response.status}`);
           }

           const data = await response.json();
           setClasses(data);
           console.log("Data: ", data);
       }
       catch(error){
           console.log("Error:",error);
           setError(error.message);
       }
       finally{
           setLoading(false);
       }
   }
    return(
        <div>
            <h2>Classes</h2>

            <button onClick={getClasses}>
                Get Classes
            </button>

            {loading &&<Loading message={"Loading Classes..."}/>}

            {hasFetched && !loading && classes.length === 0 &&(
                <p>No classes available.</p>
            )}

            {error && <ErrorMessage message={error}/>}

            {classes.map((fitnessClasses) => {
                return( <ClassCard
                        key={fitnessClasses.id}
                        id={fitnessClasses.id}
                        className={fitnessClasses.className}
                        description={fitnessClasses.description}
                        capacity={fitnessClasses.capacity}
                        confirmedBookingCount={fitnessClasses.confirmedBookingCount}
                        schedule={fitnessClasses.schedule}
                        trainers={fitnessClasses.trainers}
                        onBook={bookClass}
                        onFavorite={favoriteClass}

                />)
                }
            )}

        </div>
    );

}
    export default Classes;

/*

                   API request
                     │
          ┌──────────┼──────────┐
          ↓          ↓          ↓
       loading     success     error
          │          │          │
          ↓          ↓          ↓
      Loading...   classes    Error: ...


      To save the states/data inside component only :
    const [error,setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [classes, setClasses] = useState([]);

    To set the states i.e,  Managing and storing the Data inside the component->Classes.jsx ONlY ...
                   setError(error.message);
                   setLoading(true);
                   setClasses(data);

    To Display the data that is stored in these 3 different states :

     1.     {loading &&<p>Loading classes...</p>}
            {!loading && classes.length === 0 &&(
                <p>No classes available.</p>) }


     2.    {error && <p>Error: {error}</p>}

    3.     {classes.map((fitnessClasses) => { ....... } //ClassCard
                        |
                        |   In next step I created separate components for Error and Loading UI components whihch accept "props" parameter and rendered them inside component ->Classes.jsx
                        |   What props means : It means that data is being passed to the UI component and these components are then being rendered inside the Classes.jsx component.... for UI purpose only...
                        |  NOTICE:-> That Data/State is not being saved inside or managed inside these UI components. That is the difference.
                        |State belongs where the information is managed.   => Here it is Classes.jsx
                        | UI component receives the information through props. ==> UI components here are -> Loading, ErrorMessage , ClassCards if you look they are accepting "props" in function that means they are UI components...
                        |
                        |
                  Classes.jsx
                    │
                    ├── classes     → owns API data
                    ├── loading     → owns loading state
                    ├── error       → owns error state
                    │
                    ├── <Loading />       → displays loading
                    ├── <ErrorMessage />  → displays error
                    └── <ClassCard />     → displays class

State belongs where the information is managed.   => Here it is Classes.jsx
UI component receives the information through props. ==> UI components here are -> Loading, ErrorMessage , ClassCards if you look they are accepting "props" in function that means they are UI components...
* */

