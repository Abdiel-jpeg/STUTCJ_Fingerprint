let offset = 0;

const fetchEvento = async () => {
  const response = await fetch('SvEvento?offset=0');
  const json = await response.json();
  return json;
}

const fetchGetAsistencia = async (idEvento, offset) => {
  const response = await fetch('SvAsistencia?idEvento='+idEvento+'&offset='+offset, getParams);
  const json = await response.json();
  return json;
}

const fetchDelAsistencia = async (idEvento, nreloj) => {
  const query = {
    "option": "deleteAssistance",
    "encodedImage": "",
    "idEvento": idEvento,
    "nreloj": nreloj
  }

  const response = await fetch('SvAsistencia', getQueryParams('POST', query));
  const json = await response.json();
  return json;
}

const addEventosToSelect = async () => {
    let data = await fetchEvento()
    let selectEvento = document.getElementById('selectEvento');
    let selectedId;

    //Add initial value
    let defaultValue = document.createElement("option");
    defaultValue.value = "Selecciona el evento";
    defaultValue.selected;
    defaultValue.disabled;

    selectEvento.appendChild(defaultValue);

    for (let i = 0; i < data.length; i++) {
		let titulo = data[i].titulo;
		let optionEvento = document.createElement('option');

		optionEvento.setAttribute('value', titulo);
		optionEvento.appendChild(document.createTextNode(titulo));

		selectEvento.appendChild(optionEvento);
	}

  selectEvento.addEventListener("change", (e) => {
    console.log(e.target.selectedOptions[0].value);

    for (let i=0; i < data.length; i++) {
      evento = data[i];
      if (evento.titulo == e.target.selectedOptions[0].value) {
        selectedId = evento.id;
      }
    }

  insertSubjects(selectedId, offset);
  });
}

const insertSubjects = async (idEvento, offset) => {
  const dataSubject = await fetchGetAsistencia(idEvento, offset);

  const tbody = document.querySelector("#asistenciaTable tbody");
  tbody.innerHTML = "";
  for (let i = 0; i < dataSubject.length; i++) {
    s = dataSubject[i];
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${s.nreloj}</td>
      <td>${s.nombre}</td>
      <td>${s.apellidoPaterno}</td>
      <td>${s.apellidoMaterno}</td>
      `;
    let botonEliminar = document.createElement('button');
    botonEliminar.innerHTML = "Retirar asistencia";
    row.appendChild(document.createElement('td').appendChild(botonEliminar))

    botonEliminar.addEventListener("click", async () => {
      let response = await fetchDelAsistencia(idEvento, s.nreloj);
      console.log(response);
      alert(response.message);
    })

  tbody.appendChild(row);
  };
}

window.addEventListener("load", async () => {
  
  addEventosToSelect();   
})
