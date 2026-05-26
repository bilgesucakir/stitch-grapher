import * as THREE from 'three';
import { OrbitControls } from 'https://cdn.jsdelivr.net/npm/three@0.160.0/examples/jsm/controls/OrbitControls.js';

let rowCounter = 0;
addRow();

document.getElementById('add-row-btn').addEventListener('click', addRow);
document.getElementById('generate-graph-btn').addEventListener('click', generateGraph);

function addRow() {
  const container = document.getElementById('rows-container');

  const rowWrapper = document.createElement('div');
  rowWrapper.className = 'row-item';

  const input = document.createElement('input');
  input.type = 'text';
  input.className = 'row-input';
  input.placeholder = `Row ${rowCounter + 1}`;

  const deleteBtn = document.createElement('button');
  deleteBtn.textContent = '❌';
  deleteBtn.className = 'delete-row-btn';

  deleteBtn.addEventListener('click', () => {
    rowWrapper.remove();
    updateRowPlaceholders();
  });

  rowWrapper.appendChild(input);
  rowWrapper.appendChild(deleteBtn);
  container.appendChild(rowWrapper);

  rowCounter++;
}

function updateRowPlaceholders() {
  const inputs = document.querySelectorAll('.row-input');
  inputs.forEach((input, index) => {
    input.placeholder = `Row ${index + 1}`;
  });
  rowCounter = inputs.length;
}

function generateGraph() {

  document.querySelectorAll('.row-input').forEach(input => {
    input.classList.remove('error-input');
  });

  const errorBox = document.getElementById('error-box');
  errorBox.innerText = '';
  errorBox.classList.add('hidden');

  const rowInputs = document.querySelectorAll('.row-input');
  const rows = [];
  rowInputs.forEach(input => rows.push(input.value));

  const mode = document.getElementById('crochet-mode').value;

  fetch('/api/graph', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ rows: rows, mode: mode })
  })
    .then(async response => {
      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }
      return response.json();
    })
    .then(data => {
      if (mode === 'CIRCULAR') {
        renderCircularGraph3D(data);
      } else {
        renderFlatGraph(data);
      }
    })
    .catch(err => {
      console.error(err);

      const message = err.message || "";

      const errorBox = document.getElementById('error-box');

      // reset
      errorBox.innerText = '';
      errorBox.classList.add('hidden');

      const isRowError = message.includes("Row");

      if (isRowError) {
        highlightErrorRow(message);

        // ✅ ALSO show message
        errorBox.innerText = message;
        errorBox.classList.remove('hidden');

      } else {
        errorBox.innerText = "Something went wrong";
        errorBox.classList.remove('hidden');
      }
    });
}

function highlightErrorRow(message) {
  const inputs = document.querySelectorAll('.row-input');

  inputs.forEach(input => input.classList.remove('error-input'));

  const match = message.match(/Row (\d+)/);
  if (!match) return;

  const rowIndex = parseInt(match[1], 10);

  const input = inputs[rowIndex];

  if (!input) return;

  input.classList.add('error-input');

  setTimeout(() => {
    input.scrollIntoView({
      behavior: 'smooth',
      block: 'center'
    });
  }, 50);
}

function renderFlatGraph(data) {
  const elements = [];
  const spacing = 140;
  const rowSpacing = 140;
  const offsetY = 100;

  // 🔹 Build parent map
  const parentMap = {};
  data.edges.forEach(edge => {
    if (!parentMap[edge.target]) {
      parentMap[edge.target] = [];
    }
    parentMap[edge.target].push(edge.source);
  });

  // 🔹 Group nodes by row
  const rows = {};
  data.nodes.forEach(node => {
    if (!rows[node.row]) rows[node.row] = [];
    rows[node.row].push(node);
  });

  const sortedRows = Object.keys(rows)
    .map(Number)
    .sort((a, b) => a - b);

  const nodePositions = {};
  const totalRows = sortedRows.length;

  sortedRows.forEach((rowIndex, rowOrder) => {
    const rowNodes = rows[rowIndex];

    const isLTR = rowOrder % 2 === 0;

    let startX = 100;

    // 🔥 CORE FIX: anchor to FIRST node's parent (NOT row edge)
    if (rowOrder > 0) {
      for (const node of rowNodes) {
        const parents = parentMap[node.id];

        if (parents && parents.length > 0) {
          const parentX = nodePositions[parents[0]]?.x;

          if (parentX !== undefined) {
            startX = parentX;
            break;
          }
        }
      }
    }

    rowNodes.forEach((node, i) => {
      const x = isLTR
        ? startX + i * spacing
        : startX - i * spacing;

      const y = (totalRows - rowOrder - 1) * rowSpacing + offsetY;

      nodePositions[node.id] = { x, y };

      elements.push({
        data: { id: node.id, label: node.label },
        position: { x, y }
      });
    });
  });

  // 🔹 Edges
  data.edges.forEach(edge => {
    elements.push({
      data: { source: edge.source, target: edge.target }
    });
  });

  const container = document.getElementById('cy');
  container.innerHTML = '';

  cytoscape({
    container,
    elements,
    style: [
      {
        selector: 'node',
        style: {
          label: 'data(label)',
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
    layout: { name: 'preset' }
  });
}

function renderCircularGraph3D(data) {
  const container = document.getElementById('cy');
  container.innerHTML = '';

  const scene = new THREE.Scene();
  scene.background = new THREE.Color(0x000000);

  const width = container.clientWidth;
  const height = container.clientHeight;

  const camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 5000);

  const renderer = new THREE.WebGLRenderer({ antialias: true });
  renderer.setSize(width, height);
  container.appendChild(renderer.domElement);

  const controls = new OrbitControls(camera, renderer.domElement);
  camera.position.set(0, 200, 1200);
  camera.lookAt(0, 0, 0);
  controls.update();

  const nodeMap = {};
  const rowLengths = {};

  data.nodes.forEach(node => {
    rowLengths[node.row] = (rowLengths[node.row] || 0) + 1;
  });

  const baseRadius = 20;
  const radiusScale = 10;

  const rowHeights = {};
  let currentY = 0;

  Object.keys(rowLengths)
    .map(Number)
    .sort((a, b) => a - b)
    .forEach(row => {
      const stitchCount = rowLengths[row];

      const radius =
        baseRadius +
        Math.pow(stitchCount, 0.6) * radiusScale * 2;

      const dynamicHeight = 40 + radius * 0.2;

      rowHeights[row] = currentY;
      currentY += dynamicHeight;
    });

  const maxY = Math.max(...Object.values(rowHeights));
  const minY = Math.min(...Object.values(rowHeights));
  const centerOffset = (maxY + minY) / 2;

  data.nodes.forEach(node => {
    const stitchCount = rowLengths[node.row];

    let radius =
      baseRadius +
      Math.pow(stitchCount, 0.6) * radiusScale * 2;

    if (node.row === 0) {
      radius *= 0.5;
    }

    const angle = (2 * Math.PI * node.position) / stitchCount;

    const x = radius * Math.cos(angle);
    const z = radius * Math.sin(angle);
    const y = rowHeights[node.row] - centerOffset;

    const geometry = new THREE.SphereGeometry(18, 32, 32);
    const material = new THREE.MeshBasicMaterial({ color: 0xaaaaaa });
    const sphere = new THREE.Mesh(geometry, material);

    sphere.position.set(x, y, z);
    scene.add(sphere);

    nodeMap[node.id] = { x, y, z };
  });

  data.edges.forEach(edge => {
    const source = nodeMap[edge.source];
    const target = nodeMap[edge.target];
    if (!source || !target) return;

    const points = [
      new THREE.Vector3(source.x, source.y, source.z),
      new THREE.Vector3(target.x, target.y, target.z)
    ];

    const geometry = new THREE.BufferGeometry().setFromPoints(points);
    const material = new THREE.LineBasicMaterial({ color: 0xffffff });

    const line = new THREE.Line(geometry, material);
    scene.add(line);
  });

  window.addEventListener('resize', () => {
    const width = container.clientWidth;
    const height = container.clientHeight;

    renderer.setSize(width, height);
    camera.aspect = width / height;
    camera.updateProjectionMatrix();
  });

  function animate() {
    requestAnimationFrame(animate);
    controls.update();
    renderer.render(scene, camera);
  }

  animate();
}