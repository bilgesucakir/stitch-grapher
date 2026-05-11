import * as THREE
from 'three';

import { OrbitControls }
from 'https://cdn.jsdelivr.net/npm/three@0.160.0/examples/jsm/controls/OrbitControls.js';

let rowCounter = 0;
addRow();

document.getElementById('add-row-btn').addEventListener('click', addRow);
document.getElementById('generate-graph-btn').addEventListener('click', generateGraph);

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
  rowInputs.forEach(input => rows.push(input.value));
  const mode = document.getElementById('crochet-mode').value;
  fetch('/api/graph', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ rows: rows, mode: mode })
  })
    .then(response => response.json())
    .then(data => {
      if (mode === 'CIRCULAR') {
        renderCircularGraph3D(data);
      } else {
        renderFlatGraph(data);
      }
    })
    .catch(err => {
      console.error('Failed to generate graph', err);
    });
}

function renderFlatGraph(data) {
  const elements = [];
  const spacing = 140;
  const offsetX = 100;
  const offsetY = 100;
  const rowSpacing = 140;
  const rowLengths = {};

  data.nodes.forEach(node => { rowLengths[node.row] = (rowLengths[node.row] || 0) + 1; });
  const maxRowLength = Math.max(...Object.values(rowLengths));
  const maxRow = Math.max(...data.nodes.map(node => node.row));

  data.nodes.forEach(node => {
    const visualPosition = node.direction === 'LEFT_TO_RIGHT' ? node.position : rowLengths[node.row] - node.position - 1;
    const rowStartX = offsetX + ((maxRowLength - rowLengths[node.row]) * spacing) / 2;
    const x = rowStartX + visualPosition * spacing;
    const y = (maxRow - node.row) * rowSpacing + offsetY;
    elements.push({ data: { id: node.id, label: node.label }, position: { x: x, y: y } });
  });

  data.edges.forEach(edge => elements.push({ data: { source: edge.source, target: edge.target } }));

  const container = document.getElementById('cy');
  container.innerHTML = '';
  cytoscape({
    container: container,
    elements: elements,
    style: [
      { selector: 'node', style: { label: 'data(label)', 'text-valign': 'center', 'text-halign': 'center' } },
      { selector: 'edge', style: { 'curve-style': 'bezier', 'target-arrow-shape': 'triangle' } }
    ],
    layout: { name: 'preset' }
  });
}

function renderCircularGraph3D(data) {
  const container = document.getElementById('cy');
  container.innerHTML = '';

  const scene = new THREE.Scene();
  scene.background = new THREE.Color(0x000000);

  const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 5000);
  const renderer = new THREE.WebGLRenderer({ antialias: true });
  renderer.setSize(window.innerWidth, 900);
  container.appendChild(renderer.domElement);

  const controls = new OrbitControls(camera, renderer.domElement);
  camera.position.set(0, 200, 1200);
  camera.lookAt(0, 0, 0);
  controls.update();

  const nodeMap = {};
  const rowLengths = {};
  data.nodes.forEach(node => { rowLengths[node.row] = (rowLengths[node.row] || 0) + 1; });

  const cylinderRadius = 250;
  const rowHeight = 120;

  data.nodes.forEach(node => {
    const stitchCount = rowLengths[node.row];
    const angle = (2 * Math.PI * node.position) / stitchCount;
    const x = cylinderRadius * Math.cos(angle);
    const z = cylinderRadius * Math.sin(angle);
    const y = node.row * rowHeight;

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
    const points = [new THREE.Vector3(source.x, source.y, source.z), new THREE.Vector3(target.x, target.y, target.z)];
    const geometry = new THREE.BufferGeometry().setFromPoints(points);
    const material = new THREE.LineBasicMaterial({ color: 0xffffff });
    const line = new THREE.Line(geometry, material);
    scene.add(line);
  });

  function animate() {
    requestAnimationFrame(animate);
    controls.update();
    renderer.render(scene, camera);
  }

  animate();
}