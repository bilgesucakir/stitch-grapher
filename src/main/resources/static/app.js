import * as THREE from 'three';
import { OrbitControls } from 'https://cdn.jsdelivr.net/npm/three@0.160.0/examples/jsm/controls/OrbitControls.js';

/* ---------- Shared palette ---------- */
const STITCH_COLORS = {
  CH:   '#c4b8a3',
  SC:   '#7c99b4',
  INC:  '#7ba05b',
  DEC:  '#c77b54',
  HDC:  '#9b8bc4',
  DC:   '#8878be',
  HTR:  '#7a6bb0',
  TR:   '#6d5ea2',
  SLST: '#a0968a'
};
const DEFAULT_NODE_COLOR = '#8a8073';
const NEXT_EDGE_COLOR = '#5a514b';
const PARENT_EDGE_COLOR = '#b0704f';
const CANVAS_BG = 0x201d1b;
const NODE_HALO = '#201d1b';

const stitchColor = (label) => STITCH_COLORS[label] || DEFAULT_NODE_COLOR;

/* ---------- Row management ---------- */
let rowCounter = 0;
addRow();

document.getElementById('add-row-btn').addEventListener('click', addRow);
document.getElementById('generate-graph-btn').addEventListener('click', generateGraph);

/* ---------- How-to modal ---------- */
const HOWTO_PAGES = [
  {
    title: 'What is Stitch Grapher?',
    html: `
      <p>Stitch Grapher turns a written crochet pattern into a <strong>connectivity graph</strong> —
      a map of how every stitch attaches to its neighbours and to the row worked before it.</p>
      <h3>Flat mode</h3>
      <p>For pieces worked in rows, turning at the end of each row — scarves, blankets, granny squares.
      Row 1 is a foundation chain. The result is drawn as a <strong>2D diagram</strong>.</p>
      <h3>Circular mode</h3>
      <p>For pieces worked in continuous rounds from a centre — amigurumi, hats, coasters.
      Round 1 is a magic ring (<code>mr</code>) or a small ring of chains. The result is a
      <strong>3D model</strong> you can orbit and zoom with your mouse.</p>
    `
  },
  {
    title: 'Stitch vocabulary',
    html: `
      <p>Type one row or round per input box. Separate stitches with spaces or commas —
      <code>sc sc inc</code> and <code>sc, sc, inc</code> mean the same thing. Capitalisation doesn't matter.</p>
      <h3>Stitches</h3>
      <ul>
        <li><code>ch</code> — chain</li>
        <li><code>sc</code> — single crochet</li>
        <li><code>hdc</code> — half double &nbsp;·&nbsp; <code>dc</code> — double</li>
        <li><code>htr</code> — half treble &nbsp;·&nbsp; <code>tr</code> — treble</li>
        <li><code>slst</code> — slip stitch</li>
        <li><code>inc</code> — increase: works twice into one stitch (1 → 2)</li>
        <li><code>dec</code> — decrease: joins two stitches into one (2 → 1)</li>
        <li><code>mr</code> — magic ring: starts a round (Circular mode only)</li>
      </ul>
    `
  },
  {
    title: 'Shorthand',
    html: `
      <h3>Repeat one stitch</h3>
      <p><code>3sc</code> is the same as <code>sc sc sc</code>. Put the number first.</p>
      <h3>Repeat a group</h3>
      <p><code>(sc, inc)x6</code> repeats <code>sc inc</code> six times — twelve stitches in all.
      The <code>x6</code> after the closing bracket is required.</p>
      <p>Groups can hold shorthand too: <code>(2sc, inc)x6</code>.</p>
      <h3>Putting it together</h3>
      <p><code>(3sc, inc)x6</code> → three single crochets then an increase, repeated six times around.</p>
    `
  },
  {
    title: 'Step by step',
    html: `
      <ol>
        <li>Choose <strong>Flat</strong> or <strong>Circular</strong> at the top.</li>
        <li>In Row 1, enter your foundation — chains for Flat (e.g. <code>7ch</code>), or <code>mr</code> for Circular.</li>
        <li>Click <strong>Add row</strong> for each following row/round and type its stitches.</li>
        <li>Use <code>3sc</code> and <code>(sc, inc)x6</code> shorthand to keep rows short.</li>
        <li>Each row shows its stitch count as you type — a quick sanity check.</li>
        <li>Click <strong>Generate graph</strong>. Flat draws a <strong>2D diagram</strong>;
        Circular builds a <strong>3D model</strong> you drag to rotate and scroll to zoom.</li>
        <li>If a row doesn't add up it turns red with an explanation — fix it and generate again.</li>
      </ol>
    `
  },
  {
    title: 'Good to know',
    html: `
      <h3>Flat</h3>
      <ul>
        <li>Row 1 must be chains only.</li>
        <li>A row can't use more stitches than the row below has.</li>
      </ul>
      <h3>Circular</h3>
      <ul>
        <li>Round 1 is a single <code>mr</code>, or a ring of chains.</li>
        <li>Every round must use up exactly all the stitches from the round below.</li>
      </ul>
      <p class="howto-outro">That's it — have fun! 🧶</p>
    `
  }
];

let howtoIndex = 0;
const howtoOverlay = document.getElementById('howto-overlay');

document.getElementById('howto-btn').addEventListener('click', openHowto);
document.getElementById('howto-exit').addEventListener('click', closeHowto);
document.getElementById('howto-prev').addEventListener('click', () => {
  if (howtoIndex > 0) {
    howtoIndex--;
    renderHowto();
  }
});
document.getElementById('howto-next').addEventListener('click', () => {
  if (howtoIndex < HOWTO_PAGES.length - 1) {
    howtoIndex++;
    renderHowto();
  } else {
    closeHowto();
  }
});
howtoOverlay.addEventListener('click', (e) => {
  if (e.target === howtoOverlay) closeHowto();
});
document.addEventListener('keydown', (e) => {
  if (e.key === 'Escape' && !howtoOverlay.classList.contains('hidden')) closeHowto();
});

function openHowto() {
  howtoIndex = 0;
  renderHowto();
  howtoOverlay.classList.remove('hidden');
}

function closeHowto() {
  howtoOverlay.classList.add('hidden');
}

function renderHowto() {
  const page = HOWTO_PAGES[howtoIndex];
  document.getElementById('howto-title').textContent = page.title;

  const body = document.getElementById('howto-body');
  body.innerHTML = page.html;
  body.scrollTop = 0;

  document.getElementById('howto-dots').innerHTML = HOWTO_PAGES
    .map((_, i) => `<span class="modal-dot${i === howtoIndex ? ' active' : ''}"></span>`)
    .join('');

  document.getElementById('howto-next').textContent =
    howtoIndex === HOWTO_PAGES.length - 1 ? 'Done' : 'Next';

  document.getElementById('howto-prev').hidden = howtoIndex === 0;
}

function addRow() {
  const container = document.getElementById('rows-container');

  const rowWrapper = document.createElement('div');
  rowWrapper.className = 'row-item';

  const input = document.createElement('input');
  input.type = 'text';
  input.className = 'row-input';
  input.placeholder = `Row ${rowCounter + 1}`;
  input.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      generateGraph();
    }
  });
  input.addEventListener('input', () => updateRowCount(rowWrapper));

  const count = document.createElement('span');
  count.className = 'row-count';

  const deleteBtn = document.createElement('button');
  deleteBtn.textContent = '×';
  deleteBtn.className = 'delete-row-btn';
  deleteBtn.setAttribute('aria-label', 'Delete row');

  deleteBtn.addEventListener('click', () => {
    rowWrapper.remove();
    updateRowPlaceholders();
  });

  rowWrapper.appendChild(input);
  rowWrapper.appendChild(count);
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

/* ---------- Graph generation ---------- */
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
  const generateBtn = document.getElementById('generate-graph-btn');
  generateBtn.disabled = true;

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
      hideEmptyState();
      if (mode === 'CIRCULAR') {
        renderCircularGraph3D(data);
      } else {
        renderFlatGraph(data);
      }
    })
    .catch(err => {
      console.error(err);

      const message = err.message || "";
      errorBox.innerText = '';
      errorBox.classList.add('hidden');

      const isRowError = message.includes("Row");

      if (isRowError) {
        highlightErrorRow(message);
        errorBox.innerText = message;
        errorBox.classList.remove('hidden');
      } else {
        errorBox.innerText = "Something went wrong";
        errorBox.classList.remove('hidden');
      }
    })
    .finally(() => {
      generateBtn.disabled = false;
    });
}

/* Stitches produced by one operation, mirrors OperationType on the backend. */
const PRODUCED_OUTPUT = {
  sc: 1, inc: 2, dec: 1, hdc: 1, dc: 1, htr: 1, tr: 1, slst: 1, ch: 1, mr: 0
};

/**
 * Count the stitches a single pattern row produces, using the same expansion
 * rules as the server parser (numeric prefixes and (group)xN repeats).
 * Returns null if the row can't be parsed.
 */
function countRowStitches(text) {
  let s = text.toLowerCase().trim();
  if (s === '') return null;

  // expand (group)xN repeats, innermost first
  let guard = 0;
  const groupRe = /\(([^()]*)\)\s*x(\d+)/;
  while (groupRe.test(s)) {
    if (guard++ > 50) return null;
    s = s.replace(groupRe, (_, inner, n) => Array(Number(n)).fill(inner).join(' , '));
  }
  if (s.includes('(') || s.includes(')')) return null;

  const tokens = s.split(/[\s,]+/).filter(Boolean);
  let total = 0;

  for (let i = 0; i < tokens.length; i++) {
    let tok = tokens[i];
    let mult = 1;

    const numOnly = tok.match(/^(\d+)$/);
    if (numOnly) {
      mult = Number(numOnly[1]);
      tok = tokens[++i];
      if (tok === undefined) return null;
    } else {
      const prefixed = tok.match(/^(\d+)([a-z]+)$/);
      if (prefixed) {
        mult = Number(prefixed[1]);
        tok = prefixed[2];
      }
    }

    if (!(tok in PRODUCED_OUTPUT)) return null;
    total += PRODUCED_OUTPUT[tok] * mult;
  }

  return total;
}

function updateRowCount(item) {
  const badge = item.querySelector('.row-count');
  const input = item.querySelector('.row-input');
  if (!badge || !input) return;

  const n = countRowStitches(input.value);
  badge.textContent = n === null ? '' : `${n} st${n === 1 ? '' : 's'}`;
}

function hideEmptyState() {
  const empty = document.getElementById('canvas-empty');
  if (empty) empty.classList.add('hidden');
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
    input.scrollIntoView({ behavior: 'smooth', block: 'center' });
  }, 50);
}

/* ---------- Flat (2D) rendering ---------- */
function renderFlatGraph(data) {
  const elements = [];
  const spacing = 140;
  const rowSpacing = 140;
  const offsetY = 100;

  const rowById = {};
  data.nodes.forEach(node => { rowById[node.id] = node.row; });

  const parentMap = {};
  data.edges.forEach(edge => {
    if (!parentMap[edge.target]) parentMap[edge.target] = [];
    parentMap[edge.target].push(edge.source);
  });

  const rows = {};
  data.nodes.forEach(node => {
    if (!rows[node.row]) rows[node.row] = [];
    rows[node.row].push(node);
  });

  const sortedRows = Object.keys(rows).map(Number).sort((a, b) => a - b);
  const nodePositions = {};
  const totalRows = sortedRows.length;

  sortedRows.forEach((rowIndex, rowOrder) => {
    const rowNodes = rows[rowIndex];
    const isLTR = rowOrder % 2 === 0;
    let startX = 100;

    if (rowOrder > 0) {
      for (const node of rowNodes) {
        const parents = parentMap[node.id];
        if (parents && parents.length > 0) {
          const parentX = nodePositions[parents[0]]?.x;
          if (parentX !== undefined) { startX = parentX; break; }
        }
      }
    }

    rowNodes.forEach((node, i) => {
      const x = isLTR ? startX + i * spacing : startX - i * spacing;
      const y = (totalRows - rowOrder - 1) * rowSpacing + offsetY;
      nodePositions[node.id] = { x, y };

      elements.push({
        data: { id: node.id, label: node.label, color: stitchColor(node.label) },
        position: { x, y }
      });
    });
  });

  data.edges.forEach(edge => {
    const sameRow = rowById[edge.source] === rowById[edge.target];
    elements.push({
      data: { source: edge.source, target: edge.target },
      classes: sameRow ? 'next-edge' : 'parent-edge'
    });
  });

  const container = document.getElementById('cy');
  container.innerHTML = '';

  const cy = cytoscape({
    container,
    elements,
    minZoom: 0.15,
    maxZoom: 2.5,
    style: [
      {
        selector: 'node',
        style: {
          'background-color': 'data(color)',
          'border-width': 2,
          'border-color': NODE_HALO,
          'width': 34,
          'height': 34,
          'label': 'data(label)',
          'color': '#fff',
          'font-family': 'Inter, sans-serif',
          'font-size': 10,
          'font-weight': 600,
          'text-valign': 'center',
          'text-halign': 'center'
        }
      },
      {
        selector: 'edge',
        style: {
          'curve-style': 'bezier',
          'width': 1.6,
          'line-color': NEXT_EDGE_COLOR,
          'target-arrow-color': NEXT_EDGE_COLOR,
          'arrow-scale': 0.8
        }
      },
      {
        selector: 'edge.next-edge',
        style: {
          'line-color': NEXT_EDGE_COLOR,
          'target-arrow-shape': 'none'
        }
      },
      {
        selector: 'edge.parent-edge',
        style: {
          'line-color': PARENT_EDGE_COLOR,
          'target-arrow-color': PARENT_EDGE_COLOR,
          'target-arrow-shape': 'triangle',
          'opacity': 0.7
        }
      }
    ],
    layout: { name: 'preset', fit: true, padding: 60 }
  });

  cy.userZoomingEnabled(true);
}

/* ---------- Circular (3D) rendering ---------- */
function renderCircularGraph3D(data) {
  const container = document.getElementById('cy');
  container.innerHTML = '';

  const scene = new THREE.Scene();
  scene.background = new THREE.Color(CANVAS_BG);
  scene.fog = new THREE.Fog(CANVAS_BG, 900, 2600);

  const width = container.clientWidth;
  const height = container.clientHeight;

  const camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 5000);

  const renderer = new THREE.WebGLRenderer({ antialias: true });
  renderer.setPixelRatio(window.devicePixelRatio);
  renderer.setSize(width, height);
  container.appendChild(renderer.domElement);

  scene.add(new THREE.AmbientLight(0xffffff, 0.75));
  const keyLight = new THREE.DirectionalLight(0xffffff, 0.9);
  keyLight.position.set(400, 700, 600);
  scene.add(keyLight);
  const rimLight = new THREE.DirectionalLight(0xd8c8b4, 0.35);
  rimLight.position.set(-500, -200, -400);
  scene.add(rimLight);

  const controls = new OrbitControls(camera, renderer.domElement);
  controls.enableDamping = true;
  controls.dampingFactor = 0.08;
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

  Object.keys(rowLengths).map(Number).sort((a, b) => a - b).forEach(row => {
    const stitchCount = rowLengths[row];
    const radius = baseRadius + Math.pow(stitchCount, 0.6) * radiusScale * 2;
    const dynamicHeight = 40 + radius * 0.2;
    rowHeights[row] = currentY;
    currentY += dynamicHeight;
  });

  const maxY = Math.max(...Object.values(rowHeights));
  const minY = Math.min(...Object.values(rowHeights));
  const centerOffset = (maxY + minY) / 2;

  const sphereGeometry = new THREE.SphereGeometry(16, 24, 24);

  data.nodes.forEach(node => {
    const stitchCount = rowLengths[node.row];
    let radius = baseRadius + Math.pow(stitchCount, 0.6) * radiusScale * 2;
    if (node.row === 0) radius *= 0.5;

    const angle = (2 * Math.PI * node.position) / stitchCount;
    const x = radius * Math.cos(angle);
    const z = radius * Math.sin(angle);
    const y = rowHeights[node.row] - centerOffset;

    const material = new THREE.MeshStandardMaterial({
      color: new THREE.Color(stitchColor(node.label)),
      roughness: 0.55,
      metalness: 0.05
    });
    const sphere = new THREE.Mesh(sphereGeometry, material);
    sphere.position.set(x, y, z);
    scene.add(sphere);

    nodeMap[node.id] = { x, y, z };
  });

  const edgeMaterial = new THREE.LineBasicMaterial({ color: 0x6b615a, transparent: true, opacity: 0.8 });
  data.edges.forEach(edge => {
    const source = nodeMap[edge.source];
    const target = nodeMap[edge.target];
    if (!source || !target) return;

    const points = [
      new THREE.Vector3(source.x, source.y, source.z),
      new THREE.Vector3(target.x, target.y, target.z)
    ];
    const geometry = new THREE.BufferGeometry().setFromPoints(points);
    scene.add(new THREE.Line(geometry, edgeMaterial));
  });

  window.addEventListener('resize', () => {
    const w = container.clientWidth;
    const h = container.clientHeight;
    renderer.setSize(w, h);
    camera.aspect = w / h;
    camera.updateProjectionMatrix();
  });

  function animate() {
    requestAnimationFrame(animate);
    controls.update();
    renderer.render(scene, camera);
  }

  animate();
}
