function loadSubjects() {
  fetch("SvSubject?offset=0&limit=100")
    .then((res) => res.json())
    .then((subjects) => {
      const tbody = document.querySelector("#subjectsTable tbody");
      tbody.innerHTML = "";
      subjects.forEach((sub) => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${sub.nreloj}</td>
          <td>${sub.nombre}</td>
          <td>${sub.apellidoPaterno}</td>
          <td>${sub.apellidoMaterno}</td>
          <td class="${
            sub.activated ? "activo" : "inactivo"
          }">${sub.activated ? "Sí" : "No"}</td>
          <td><button onclick='location.href="formulario.html?nreloj=${sub.nreloj}"'>Modificar</button></td>
        `;
        tbody.appendChild(row);
      });
    })
    .catch((err) => console.error("Error cargando Subjects:", err));
}

window.addEventListener("load", () => {
	loadSubjects();
})