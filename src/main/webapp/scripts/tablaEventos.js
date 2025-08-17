
let count, offset, limit;

const fetchEventos = async () => {
  const response = await fetch('SvEvento?offset=' + offset);
  const json = await response.json();
  return json;
}

const insertPageButtons = (parent) => {	
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

	previousPage.addEventListener("click", (e) => {
		e.preventDefault();

		goToPreviousPage();
	})

	nextPage.addEventListener("click", (e) => {
		e.preventDefault();

		goToNextPage();
	})

	containerPages.appendChild(labelPages);
	containerPages.appendChild(previousPage);
	containerPages.appendChild(nextPage);

	parent.appendChild(containerPages);
}

const goToPreviousPage = () => {
	offset = offset - limit;

  location.href = 'tabla.html?offset=' + offset;
}

const goToNextPage = () => {
	offset = offset + limit;

	location.href = 'tabla.html?offset=' + offset;
}

const loadEventos = async () => {
  let data = await fetchEventos();

  count = data.count;
  limit = data.eventos.length;
    
  const tbody = document.querySelector("#eventoTable tbody");
  tbody.innerHTML = "";

  for (let i = 0; i < data.eventos.length; i++) {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${data.eventos[i].id}</td>
      <td>${data.eventos[i].titulo}</td>
      <td>${data.eventos[i].descripcion}</td>
      <td><button onclick='location.href="formularioEventos.html?id=${data.eventos[i].id}"'>Modificar</button></td>
    `;

    tbody.appendChild(row);
  }
}

window.addEventListener("load", async () => {
  let pages = document.getElementById('pages');
  let url = window.location;
  let params = new URLSearchParams(url.search);
  let offsetParam = parseInt(params.get('offset'));
    
  offset = offsetParam || offsetParam < 0 ? offsetParam : 0;

	await loadEventos();
  insertPageButtons(pages);
})

window.addEventListener("pageshow", (e) => {
  if (e.persisted) {
    location.reload();
  }
})