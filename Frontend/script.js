// Shared script for login, register, and todos pages
const SERVER_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

// Login page logic
function login() 
{
    const email=document.getElementById("email").value;
    const password=document.getElementById("password").value;
    //frontend to backend call
    fetch(`${SERVER_URL}/auth/login`,
        {
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body: JSON.stringify({email,password})
        }
    ).then(response => {   // then = if response is received
        if(!response.ok)  // and its not of status ok
        {
            return response.json().then(data=>{throw new Error(data.message || "Login failed")});
        }
        return response.json();
    })
    .then(data=>{  //got ok status response
        localStorage.setItem("token",data.token); //take and set token in local storage
        window.location.href="todos.html"; //redirect to todo page upon successful login
    })
    .catch(error=>{ //if no response is received
        alert(error.message);
    })
}

// Register page logic
function register() 
{
    const email=document.getElementById("email").value;
    const password=document.getElementById("password").value;
    //frontend to backend call
    fetch(`${SERVER_URL}/auth/register`,
        {
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body: JSON.stringify({email,password})
        })
    .then(response => {   // then = if response is received
        if(response.ok)
        {
            alert("Registration successful, Please login");
            window.location.href="login.html";
        }
        else
        {
            return response.json().then(data=>{throw new Error(data.message || "Registration failed")});
        }
    })
    .catch(error=>{ //if no response is received
        alert(error.message);
    })
}

// Todos page logic- to create the card in frontend
function createTodoCard(todo) 
{
    const card = document.createElement("div");
    card.className="todo-card";

    const checkbox = document.createElement("input");
    checkbox.type="checkbox";
    checkbox.checked=todo.isCompleted; // if true, checkbox=ticked
    checkbox.addEventListener("change", function(){
        const updatedTodo={...todo, isCompleted:checkbox.checked}
        updateTodoStatus(updatedTodo);
    });

    const span=document.createElement("span");
    span.textContent=todo.title;
    if(todo.isCompleted)
    {
        span.style.textDecoration="Line-through";
        span.style.color="#aaa";
    }

    const delBtn=document.createElement("button");
    delBtn.textContent="X";
    delBtn.style.color="#000000e6";
    delBtn.style.backgroundColor="#e75550e6";
    delBtn.onclick=function(){
        deleteTodo(todo.id);
    }

    card.appendChild(checkbox);
    card.appendChild(span);
    card.appendChild(delBtn);

    return card;
}


function loadTodos() 
{
    if(!token)
    {
        alert("Please login first");
        window.location.href="login.html";
        return;
    }

    fetch(`${SERVER_URL}/api/v1/todo/getall`,
        {
            method:"GET",
            headers:{
                    "Authorization":`Bearer ${token}`
                    }
        }
    ).then(response => {   // then = if response is received
        if(!response.ok)  // and its not of status ok
        {
            return response.json().then(data=>{throw new Error(data.message || "Failed to get todos")});
        }
        return response.json();
    })
    .then((todos) => {
        const todoList=document.getElementById("todo-list");
        todoList.innerHTML="";

        if(!todos || todos.length===0)
            todoList.innerHTML="<p id='empty-message'>No todos yet, add one below</p>"
        else
        {
            //create card for each todo
            todos.forEach(todo =>{
                todoList.appendChild(createTodoCard(todo));
            });
        }
    })
    .catch(error=>{ //if no response is received
        document.getElementById("todo-list").innerHTML="<p style='color:red'>Failed to load todos</p>";
    });

}

function addTodo() 
{
    const input=document.getElementById("new-todo");
    const todoText=input.value.trim();
    if(!todoText)
        return;

    fetch(`${SERVER_URL}/api/v1/todo/create`,
        {
            method:"POST",
            headers:{
                    "Content-Type":"application/json",
                    "Authorization":`Bearer ${token}`
                    },
            body:JSON.stringify({title:todoText, isCompleted:false})
        }
    ).then(response => {   // then = if response is received
        if(!response.ok)  // and its not of status ok
        {
            return response.json().then(data=>{throw new Error(data.message || "Failed to create new todo")});
        }
        return response.json();
    })
    .then((newTodo) => {
        input.value="";
        loadTodos(); 
    })
    .catch(error=>{ //if no response is received
        alert(error.message);
    });
}

function updateTodoStatus(todo) 
{
    fetch(`${SERVER_URL}/api/v1/todo/update`,
        {
            method:"PUT",
            headers:{
                    "Content-Type":"application/json",
                    "Authorization":`Bearer ${token}`
                    },
            body:JSON.stringify(todo)
        }
    ).then(response => {   // then = if response is received
        if(!response.ok)  // and its not of status ok
        {
            return response.json().then(data=>{throw new Error(data.message || "Failed to update todo")});
        }
        return response.json();
    })
    //entry will be updated in DB, but has to be refelcted in frontend also, so loadTodo again from DB
    .then(()=>loadTodos()) 
    .catch(error=>{ //if no response is received
        alert(error.message);
    });
}

function deleteTodo(id) 
{
    fetch(`${SERVER_URL}/api/v1/todo/delete/${id}`,
        {
            method:"DELETE",
            headers:{
                    "Authorization":`Bearer ${token}`
                    }
        }
    ).then(response => {   // then = if response is received
        if(!response.ok)  // and its not of status ok
        {
            return response.json().then(data=>{throw new Error(data.message || "Failed to delete todo")});
        }
        return response.text(); //dont use .json here
    })
    //entry will be deleted in DB, but has to be updated in frontend also, so loadTodo again from DB
    .then(()=>loadTodos()) 
    .catch(error=>{ //if no response is received
        alert(error.message);
    });
}

// Page-specific initializations
document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("todo-list")) {
        loadTodos();
    }
});
