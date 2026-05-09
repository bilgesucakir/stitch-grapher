let rowCounter = 0;

addRow();

function addRow() {
  const container = document.getElementById('rows-container');
  const input = document.createElement('input');
  input.type = 'text';
  input.className = 'row-input';
  input.placeholder = `Row ${rowCounter + 1}`;
  container.appendChild(input);
  rowCounter++;
}

function generateGraph() {
  const rowInputs = document.querySelectorAll('.row-input');
  const rows = [];
  rowInputs.forEach(input => {
    rows.push(input.value);
  });

  fetch('/api/graph', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ rows: rows })
  })
    .then(response => response.json())
    .then(renderGraph);
}

function renderGraph(data) {
  const elements = [];
  const spacing = 140;
  const offsetX = 100;
  const offsetY = 100;
  const rowSpacing = 140;
  const rowLengths = {};

  data.nodes.forEach(node => {
    if (!rowLengths[node.row]) {
      rowLengths[node.row] = 0;
    }
    rowLengths[node.row]++;
  });

  const maxRowLength = Math.max(...Object.values(rowLengths));
  const maxRow = Math.max(...data.nodes.map(node => node.row));

  data.nodes.forEach(node => {
    let visualPosition;

    // Mirror turned rows visually.
    if (node.direction === 'LEFT_TO_RIGHT') {
      visualPosition = node.position;
    } else {
      visualPosition = rowLengths[node.row] - node.position - 1;
    }

    const rowStartX = offsetX + ((maxRowLength - rowLengths[node.row]) * spacing) / 2;
    const x = rowStartX + visualPosition * spacing;
    const y = (maxRow - node.row) * rowSpacing + offsetY;

    elements.push({
      data: {
        id: node.id,
        label: node.label
      },
      position: { x: x, y: y }
    });
  });

  data.edges.forEach(edge => {
    elements.push({
      data: {
        source: edge.source,
        target: edge.target
      }
    });
  });

  document.getElementById('cy').innerHTML = '';

  cytoscape({
    container: document.getElementById('cy'),
    elements: elements,
    style: [
      {
        selector: 'node',
        style: {
          'label': 'data(label)',
          'text-valign': 'center',
          'text-halign': 'center'
        }
      },
      {
        selector: 'edge',
        style: {
          'curve-style': 'bezier',
          'target-arrow-shape': 'triangle'
        }
      }
    ],
    layout: {
      name: 'preset'
    }
  });
}