let currentUserId = null;

function logout() {
    currentUserId = null;
    document.getElementById('login-username').value = ""; // Golește câmpul de login
    changeScene('login');
}

function changeScene(s) {
    document.querySelectorAll('.scene').forEach(x => x.classList.remove('active'));
    document.getElementById('scene-' + s).classList.add('active');
    if(s === 'admin') loadUsers();
    if(s === 'user') loadProjects();
}

async function login(role) {
    const userIn = document.getElementById('login-username').value;
    try {
        const res = await fetch('/api/users');
        const users = await res.json();
        const found = users.find(u => u.username === userIn);

        if (found) {
            currentUserId = found.id;
            changeScene(role.toLowerCase());
        } else {
            alert("User invalid! Incearca 'admin'.");
        }
    } catch (e) { alert("Server offline!"); }
}

async function addProject() {
    const t = document.getElementById('p-title').value;
    const d = document.getElementById('p-desc').value;
    if (!currentUserId) return;

    await fetch(`/api/add-project?title=${encodeURIComponent(t)}&desc=${encodeURIComponent(d)}&uid=${currentUserId}`);
    alert("Proiect adaugat!");
    loadProjects();
}

async function loadUsers() {
    const res = await fetch('/api/users');
    const data = await res.json();
    document.getElementById('admin-user-list').innerHTML = data.map(u => `<div class="card">${u.displayName} (@${u.username})</div>`).join('');
}

async function loadProjects() {
    const res = await fetch('/api/projects');
    const data = await res.json();
    document.getElementById('user-project-list').innerHTML = data.map(p => `
        <div class="card"><h3>${p.title}</h3><p>${p.desc}</p><small>De la: ${p.owner}</small></div>`).join('');
}

async function addUser() {
    const n = document.getElementById('admin-new-name').value;
    const u = document.getElementById('admin-new-user').value;
    await fetch(`/api/add-user?name=${encodeURIComponent(n)}&user=${encodeURIComponent(u)}`);
    loadUsers();
}