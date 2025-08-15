function loadSubjects() {
  fetch("SvEvento?offset=0")
    .then((res) => res.json())
    .then((eventos) => {
      const tbody = document.querySelector("#eventoTable tbody");
      tbody.innerHTML = "";
      eventos.forEach((evento) => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${evento.id}</td>
          <td>${evento.titulo}</td>
          <td>${evento.descripcion}</td>
          <td><button onclick='location.href="formularioEventos.html?id=${evento.id}"'>Modificar</button></td>
        `;
        tbody.appendChild(row);
      });
    })
    .catch((err) => console.error("Error cargando Subjects:", err));
}

window.addEventListener("load", () => {
	loadSubjects();
})