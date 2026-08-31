/* ============================================================
   DSA Interview Canvas — Shared Interactive Layer
   Vanilla JS, no dependencies. Each behaviour is opt-in.
   ============================================================ */
(function () {
  'use strict';
  var $  = function (s, r) { return (r || document).querySelector(s); };
  var $$ = function (s, r) { return Array.prototype.slice.call((r || document).querySelectorAll(s)); };

  // ---- tabs ----
  function initTabs() {
    $$('.tabs').forEach(function (tabs) {
      var scope = tabs.closest('[data-tabscope]') || document;
      tabs.addEventListener('click', function (e) {
        var t = e.target.closest('.tab'); if (!t) return;
        var id = t.getAttribute('data-tab');
        $$('.tab', tabs).forEach(function (x) { x.classList.toggle('active', x === t); });
        $$('.tabpanel', scope).forEach(function (p) { p.classList.toggle('active', p.getAttribute('data-panel') === id); });
      });
    });
  }

  // ---- collapsibles ----
  function initCollapsibles() {
    $$('.collapsible > .c-head').forEach(function (h) {
      h.addEventListener('click', function () { h.parentElement.classList.toggle('open'); });
    });
  }

  // ---- LRU cache simulator ----
  function initLRUSim() {
    var wrap = document.getElementById('lru-sim');
    if (!wrap) return;

    var cap = 3;
    var cache = [];  // [{key, value}] ordered most-recent-first
    var map = {};
    var log = $('#lru-log');
    var viz = $('#lru-viz');

    function addLog(msg, cls) {
      var d = document.createElement('div');
      d.className = 'entry ' + (cls || '');
      d.textContent = msg;
      log.appendChild(d);
      log.scrollTop = log.scrollHeight;
    }

    function render() {
      var html = '<svg width="' + Math.max(500, cache.length * 110 + 140) + '" height="80">';
      html += '<defs><marker id="ah" viewBox="0 0 10 10" refX="10" refY="5" markerWidth="8" markerHeight="8" orient="auto"><path d="M0,0 L10,5 L0,10 Z" fill="#8b95a3"/></marker></defs>';
      // dummy HEAD
      html += '<rect x="10" y="20" width="50" height="40" class="dummy"/>';
      html += '<text x="35" y="45" text-anchor="middle" fill="#8b95a3" font-size="11">HEAD</text>';
      var x = 70;
      cache.forEach(function (item, i) {
        html += '<line x1="' + x + '" y1="40" x2="' + (x + 10) + '" y2="40" class="ptr" marker-end="url(#ah)"/>';
        x += 15;
        html += '<rect x="' + x + '" y="20" width="80" height="40" class="node-hl"/>';
        html += '<text x="' + (x + 40) + '" y="37" text-anchor="middle" font-size="12" font-weight="600" fill="#2dd4bf">' + item.key + '</text>';
        html += '<text x="' + (x + 40) + '" y="52" text-anchor="middle" font-size="11" fill="#b8c1cc">val=' + item.value + '</text>';
        x += 80;
      });
      // dummy TAIL
      html += '<line x1="' + x + '" y1="40" x2="' + (x + 10) + '" y2="40" class="ptr" marker-end="url(#ah)"/>';
      x += 15;
      html += '<rect x="' + x + '" y="20" width="50" height="40" class="dummy"/>';
      html += '<text x="' + (x + 25) + '" y="45" text-anchor="middle" fill="#8b95a3" font-size="11">TAIL</text>';
      html += '</svg>';
      viz.innerHTML = html;
    }

    function simGet() {
      var key = parseInt($('#lru-key').value, 10);
      if (isNaN(key)) return;
      if (map[key] !== undefined) {
        var idx = cache.findIndex(function (n) { return n.key === key; });
        var node = cache.splice(idx, 1)[0];
        cache.unshift(node);
        addLog('get(' + key + ') = ' + node.value + '  [HIT — moved to head]', 'hit');
      } else {
        addLog('get(' + key + ') = -1  [MISS]', 'miss');
      }
      render();
    }

    function simPut() {
      var key = parseInt($('#lru-key').value, 10);
      var val = parseInt($('#lru-val').value, 10);
      if (isNaN(key) || isNaN(val)) return;
      if (map[key] !== undefined) {
        var idx = cache.findIndex(function (n) { return n.key === key; });
        cache.splice(idx, 1);
        cache.unshift({ key: key, value: val });
        map[key] = val;
        addLog('put(' + key + ',' + val + ')  [UPDATE — moved to head]', 'put');
      } else {
        if (cache.length >= cap) {
          var evicted = cache.pop();
          delete map[evicted.key];
          addLog('  evicted key=' + evicted.key + ' (LRU)', 'evict');
        }
        cache.unshift({ key: key, value: val });
        map[key] = val;
        addLog('put(' + key + ',' + val + ')  [NEW — added to head]', 'put');
      }
      render();
    }

    function simReset() {
      cache = []; map = {}; log.innerHTML = '';
      addLog('Cache reset (capacity=' + cap + ')', '');
      render();
    }

    $('#lru-get').addEventListener('click', simGet);
    $('#lru-put').addEventListener('click', simPut);
    $('#lru-reset').addEventListener('click', simReset);
    var capInput = $('#lru-cap');
    if (capInput) capInput.addEventListener('change', function () {
      cap = Math.max(1, parseInt(capInput.value, 10) || 3);
      simReset();
    });
    render();
    addLog('LRU Cache ready (capacity=' + cap + '). Try put/get!', '');
  }

  // ---- LFU cache simulator ----
  function initLFUSim() {
    var wrap = document.getElementById('lfu-sim');
    if (!wrap) return;

    var cap = 3;
    var keyVal = {}, keyFreq = {}, freqKeys = {}, minFreq = 0, order = {};
    var seq = 0;
    var log = $('#lfu-log');
    var viz = $('#lfu-viz');

    function addLog(msg, cls) {
      var d = document.createElement('div');
      d.className = 'entry ' + (cls || '');
      d.textContent = msg;
      log.appendChild(d);
      log.scrollTop = log.scrollHeight;
    }

    function render() {
      var freqs = Object.keys(freqKeys).map(Number).sort(function (a, b) { return a - b; });
      var html = '';
      if (freqs.length === 0) {
        html = '<div style="color:var(--faint);font-size:13px">Cache empty — frequency buckets will appear here</div>';
      } else {
        freqs.forEach(function (f) {
          var keys = freqKeys[f];
          if (!keys || keys.length === 0) return;
          var isMin = f === minFreq;
          var borderColor = isMin ? 'var(--warn)' : 'var(--border)';
          html += '<div style="border:1px solid ' + borderColor + ';border-radius:8px;padding:10px 12px;margin:6px 0;background:var(--panel-2)">';
          html += '<div style="font-size:11px;text-transform:uppercase;color:' + (isMin ? 'var(--warn)' : 'var(--faint)') + ';margin-bottom:6px">freq=' + f + (isMin ? ' (min — evict from here)' : '') + '</div>';
          html += '<div style="display:flex;gap:8px;flex-wrap:wrap">';
          keys.forEach(function (k) {
            html += '<span style="font-family:monospace;font-size:13px;padding:4px 10px;border-radius:6px;background:var(--panel);border:1px solid var(--border)">';
            html += '<span style="color:var(--accent)">' + k + '</span>=<span style="color:var(--muted)">' + keyVal[k] + '</span></span>';
          });
          html += '</div></div>';
        });
      }
      viz.innerHTML = html;
    }

    function increaseFreq(key) {
      var f = keyFreq[key], nf = f + 1;
      keyFreq[key] = nf;
      var old = freqKeys[f];
      old.splice(old.indexOf(key), 1);
      if (old.length === 0) { delete freqKeys[f]; if (f === minFreq) minFreq++; }
      if (!freqKeys[nf]) freqKeys[nf] = [];
      freqKeys[nf].push(key);
    }

    function simGet() {
      var key = parseInt($('#lfu-key').value, 10);
      if (isNaN(key)) return;
      if (keyVal[key] !== undefined) {
        increaseFreq(key);
        addLog('get(' + key + ') = ' + keyVal[key] + '  [HIT — freq now ' + keyFreq[key] + ']', 'hit');
      } else {
        addLog('get(' + key + ') = -1  [MISS]', 'miss');
      }
      render();
    }

    function simPut() {
      var key = parseInt($('#lfu-key').value, 10);
      var val = parseInt($('#lfu-val').value, 10);
      if (isNaN(key) || isNaN(val)) return;
      if (keyVal[key] !== undefined) {
        keyVal[key] = val;
        increaseFreq(key);
        addLog('put(' + key + ',' + val + ')  [UPDATE — freq now ' + keyFreq[key] + ']', 'put');
      } else {
        var size = Object.keys(keyVal).length;
        if (size >= cap) {
          var bucket = freqKeys[minFreq];
          var evictKey = bucket.shift();
          if (bucket.length === 0) delete freqKeys[minFreq];
          delete keyVal[evictKey];
          delete keyFreq[evictKey];
          addLog('  evicted key=' + evictKey + ' (freq=' + minFreq + ', LFU)', 'evict');
        }
        keyVal[key] = val;
        keyFreq[key] = 1;
        if (!freqKeys[1]) freqKeys[1] = [];
        freqKeys[1].push(key);
        minFreq = 1;
        addLog('put(' + key + ',' + val + ')  [NEW — freq=1]', 'put');
      }
      render();
    }

    function simReset() {
      keyVal = {}; keyFreq = {}; freqKeys = {}; minFreq = 0;
      log.innerHTML = '';
      addLog('LFU Cache reset (capacity=' + cap + ')', '');
      render();
    }

    $('#lfu-get').addEventListener('click', simGet);
    $('#lfu-put').addEventListener('click', simPut);
    $('#lfu-reset').addEventListener('click', simReset);
    var capInput = $('#lfu-cap');
    if (capInput) capInput.addEventListener('change', function () {
      cap = Math.max(1, parseInt(capInput.value, 10) || 3);
      simReset();
    });
    render();
    addLog('LFU Cache ready (capacity=' + cap + '). Try put/get!', '');
  }

  document.addEventListener('DOMContentLoaded', function () {
    initTabs();
    initCollapsibles();
    initLRUSim();
    initLFUSim();
  });
})();
