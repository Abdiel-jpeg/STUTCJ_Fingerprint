let limit = 0;
let count = 0;
let offset = 0;
let idEvento = 0;
let pagesDiv;

const fetchEvento = async () => {
  const response = await fetch('SvEvento?offset=0');
  const json = await response.json();
  return json;
}

const fetchGetAsistencia = async () => {
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

const insertPageButtons = () => {	
  pagesDiv.innerHTML = "";

	let containerPages = document.createElement('page');
	let labelPages = document.createElement('label');
	let previousPage = document.createElement('button');
	let nextPage = document.createElement('button');

	containerPages.setAttribute('class', 'containerPages');
	labelPages.setAttribute('id', 'labelPages');
	previousPage.setAttribute('class', 'previousPage');
	nextPage.setAttribute('class', 'nextPage');

	labelPages.appendChild(document.createTextNode(`Mostrando ${offset + limit > count ? count : offset + limit} de ${count}`))

  //Si estamos en la primera pagina no mostrar atras
	if (offset == 0) {
		previousPage.style.display = 'none';
	}

  //Si los rows son menos que el limite no mostrar ni mrd
  //Y en caso de que estemos en la última página tampoco mostrarlo
	if (count <= limit || offset + limit > count) {
		nextPage.style.display = 'none';
	}

	previousPage.appendChild(document.createTextNode('<'));
	nextPage.appendChild(document.createTextNode('>'))

	previousPage.addEventListener("click", (e) => {
		e.preventDefault();

		offset = offset - limit;

    insertSubjects();
    insertPageButtons();
	})

	nextPage.addEventListener("click", (e) => {
		e.preventDefault();

		offset = offset + limit;

    //Update table
    insertSubjects();
    insertPageButtons();
	})

	containerPages.appendChild(labelPages);
	containerPages.appendChild(previousPage);
	containerPages.appendChild(nextPage);

	pagesDiv.appendChild(containerPages);
}

const addEventosToSelect = async () => {
    let { eventos } = await fetchEvento()
    let selectEvento = document.getElementById('selectEvento');

    //Add initial value
    let defaultValue = document.createElement("option");
    defaultValue.value = "Selecciona el evento";
    defaultValue.selected = true;
    defaultValue.disabled = true;

    selectEvento.appendChild(defaultValue);

    for (let i = 0; i < eventos.length; i++) {
		let titulo = eventos[i].titulo;
		let optionEvento = document.createElement('option');

		optionEvento.setAttribute('value', titulo);
    optionEvento.setAttribute('id', eventos[i].id);
		optionEvento.appendChild(document.createTextNode(titulo));

		selectEvento.appendChild(optionEvento);
	}

  selectEvento.addEventListener("change", (e) => {
    idEvento = e.target.selectedOptions[0].id;

    //Update table
    insertSubjects();
    insertPageButtons();
  });
}

const insertSubjects = async () => {
  let { count, subjects } = await fetchGetAsistencia();

  count = count;
  limit = subjects.length;

  const tbody = document.querySelector("#asistenciaTable tbody");
  tbody.innerHTML = "";

  for (let i = 0; i < subjects.length; i++) {
    s = subjects[i];
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

      //Update table
      insertSubjects();
      insertPageButtons();
    })

  tbody.appendChild(row);
  };
}

window.addEventListener("load", async () => {
  pagesDiv = document.getElementById('pages');

  addEventosToSelect();   
})
