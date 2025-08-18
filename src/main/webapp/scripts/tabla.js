
let limit = 0;
let offset = 0;
let count = 0;
let pages;
let currentSearchQuery = null;

const fetchSubjects = async () => {
  const response = await fetch("SvSubject?offset=" + offset, getParams);
  const json = await response.json();
  return json;
}

const fetchSubjectsSearchQuery = async (searchQuery) => {
  const response = await fetch("SvSubject?offset=" + offset + "&searchQuery=" + searchQuery, getParams);
  const json = await response.json();
  return json;
}

const insertPageButtons = () => {	
  pages.innerHTML = "";

	let containerPages = document.createElement('page');
	let labelPages = document.createElement('label');
	let previousPage = document.createElement('button');
	let nextPage = document.createElement('button');

	containerPages.setAttribute('class', 'containerPages');
	labelPages.setAttribute('id', 'labelPages');
	previousPage.setAttribute('class', 'previousPage');
	nextPage.setAttribute('class', 'nextPage');

  console.log("offset: " +  offset + ".count: " + count + ". limit: " + limit);
  console.log('offset is number: ' + Number.isInteger(offset));
  console.log('count is number: ' + Number.isInteger(count));
  console.log('limit is number: ' + Number.isInteger(limit));

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

	previousPage.addEventListener("click", async (e) => {
		e.preventDefault();

	  offset = offset - limit;

    await loadSubjects();
    insertPageButtons();
	})

	nextPage.addEventListener("click", async (e) => {
		e.preventDefault();

	  offset = offset + limit;

    await loadSubjects();
    insertPageButtons();
	})

	containerPages.appendChild(labelPages);
	containerPages.appendChild(previousPage);
	containerPages.appendChild(nextPage);

	pages.appendChild(containerPages);
}

const loadSubjects = async () => {
  let data;

  //If there where a search query
  if (currentSearchQuery !== null) {
    data = currentSearchQuery;
  
    //If this method was called without search query
  } else {
    data = await fetchSubjects();
  }

  count = data.count;
  limit = data.limit;

  const tbody = document.querySelector("#subjectsTable tbody");
  tbody.innerHTML = "";
  
  for (let i = 0; i < data.subjects.length; i++) {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${data.subjects[i].nreloj}</td>
      <td>${data.subjects[i].nombre}</td>
      <td>${data.subjects[i].apellidoPaterno}</td>
      <td>${data.subjects[i].apellidoMaterno}</td>
      <td class="${
        data.subjects[i].activated ? "activo" : "inactivo"
      }">${data.subjects[i].activated ? "Sí" : "No"}</td>
      <td><button onclick='location.href="formulario.html?nreloj=${data.subjects[i].nreloj}"'>Modificar</button></td>
    `;

    tbody.appendChild(row);
  }
}

window.addEventListener("load", async () => {
  let url = window.location;
  let params = new URLSearchParams(url.search);
  let offsetParam = parseInt(params.get('offset'));
  offset = offsetParam || offsetParam < 0 ? offsetParam : 0;

  pages = document.getElementById('pages');

  document.getElementById("buscarButton").addEventListener("click", async () => {
    const searchQuery = document.getElementById("buscarInput").value;

    let data = await fetchSubjectsSearchQuery(searchQuery);

    console.log(data)

    currentSearchQuery = data;
    limit = data.limit
    count = data.count

    await loadSubjects();
    insertPageButtons();
  })

	await loadSubjects();
  insertPageButtons();
})

window.addEventListener("pageshow", (e) => {
  if (e.persisted) {
    location.reload();
  }
})
