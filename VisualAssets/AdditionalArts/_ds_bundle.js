/* @ds-bundle: {"format":4,"namespace":"QuantumEffectDesignSystem_2d830e","components":[{"name":"Button","sourcePath":"components/core/Button.jsx"},{"name":"MenuList","sourcePath":"components/core/MenuList.jsx"},{"name":"Tabs","sourcePath":"components/core/Tabs.jsx"},{"name":"DialogueBox","sourcePath":"components/dialogue/DialogueBox.jsx"},{"name":"StatMeter","sourcePath":"components/equipment/StatMeter.jsx"},{"name":"ModChip","sourcePath":"components/equipment/StatMeter.jsx"},{"name":"Notification","sourcePath":"components/hud/Notification.jsx"},{"name":"LootPopup","sourcePath":"components/hud/Notification.jsx"},{"name":"Panel","sourcePath":"components/hud/Panel.jsx"},{"name":"StatBar","sourcePath":"components/hud/StatBar.jsx"},{"name":"ItemSlot","sourcePath":"components/inventory/ItemSlot.jsx"},{"name":"RarityTag","sourcePath":"components/inventory/ItemSlot.jsx"},{"name":"ElementCard","sourcePath":"components/quantum/ElementCard.jsx"},{"name":"HazardBadge","sourcePath":"components/quantum/ElementCard.jsx"},{"name":"ObjectiveList","sourcePath":"components/quests/ObjectiveList.jsx"},{"name":"QuestCard","sourcePath":"components/quests/QuestCard.jsx"},{"name":"QuestRewards","sourcePath":"components/quests/QuestRewards.jsx"},{"name":"QuestStatus","sourcePath":"components/quests/QuestTracker.jsx"},{"name":"QuestTracker","sourcePath":"components/quests/QuestTracker.jsx"},{"name":"LoadingScreen","sourcePath":"components/system/LoadingScreen.jsx"},{"name":"Minimap","sourcePath":"components/system/Minimap.jsx"}],"sourceHashes":{"assets/data/content.js":"9b36cac0726c","components/core/Button.jsx":"5cb8a0408688","components/core/MenuList.jsx":"5a9a6ca298f9","components/core/Tabs.jsx":"42d81cb4bc49","components/dialogue/DialogueBox.jsx":"f6251cb34a0d","components/equipment/StatMeter.jsx":"dd2c0b4a05dc","components/hud/Notification.jsx":"08fbf5466e52","components/hud/Panel.jsx":"fbb0ab2233b4","components/hud/StatBar.jsx":"16d43ec0c53c","components/inventory/ItemSlot.jsx":"9318b19b3687","components/quantum/ElementCard.jsx":"91c836fd1672","components/quests/ObjectiveList.jsx":"9df6219fb0c0","components/quests/QuestCard.jsx":"1c29508595ff","components/quests/QuestRewards.jsx":"30c9e2a19394","components/quests/QuestTracker.jsx":"b846b8be8982","components/system/LoadingScreen.jsx":"0cb8de892f6d","components/system/Minimap.jsx":"1e422168484a","ui_kits/game/DialogueScene.jsx":"d4024b047907","ui_kits/game/ExplorationHUD.jsx":"cbe68a7edebf","ui_kits/game/Inventory.jsx":"20bd328ecbc1","ui_kits/game/MainMenu.jsx":"3461db56f2ab","ui_kits/game/QuestLog.jsx":"2ebe45905070","ui_kits/game/kit.jsx":"a9b86a38c61b"},"inlinedExternals":[],"unexposedExports":[]} */

(() => {

const __ds_ns = (window.QuantumEffectDesignSystem_2d830e = window.QuantumEffectDesignSystem_2d830e || {});

const __ds_scope = {};

(__ds_ns.__errors = __ds_ns.__errors || []);

// assets/data/content.js
try { (() => {
// Canonical game-content loader for the Quantum Effect Design System.
// Browser:  const C = await QEContent.load('../assets/data');  // base = path to /data
// Node/build: require the JSON files directly; they are plain data.
(function (root) {
  const FILES = ['factions', 'companions', 'bosses', 'elements', 'rarities', 'items', 'regions', 'characters', 'quests'];
  async function fetchJSON(url) {
    const r = await fetch(url);
    if (!r.ok) throw new Error('QEContent: failed to load ' + url + ' (' + r.status + ')');
    return r.json();
  }
  async function load(base) {
    base = (base || '.').replace(/\/$/, '');
    const out = {};
    await Promise.all(FILES.map(async k => Object.assign(out, await fetchJSON(base + '/' + k + '.json'))));
    return out; // { factions, companions, bosses, elements, hazards, rarities, items, worldmaps, regions, portraitGroups, quests }
  }
  async function loadCatalog(base) {
    base = (base || '.').replace(/\/$/, '');
    return (await fetchJSON(base + '/catalog.json')).galleries; // { <key>: { base, label, entries[] } }
  }
  // UI-sound routing: map a UI event name to its cue file. cue('ui.confirm') -> 'sfx/ui_confirm.wav'
  async function loadAudio(base) {
    base = (base || '.').replace(/\/$/, '');
    const d = await fetchJSON(base + '/audio.json');
    const cue = event => (d.cues[event] || {}).file;
    return {
      cues: d.cues,
      categories: d.categories,
      cue
    };
  }
  // Localized UI copy. loadStrings(base,'ja') falls back to strings.json (en) if a locale file is missing.
  // Use t('menu.new_game') in place of hard-coded strings; t(key, fallback) returns fallback (or the key) when absent.
  async function loadStrings(base, locale) {
    base = (base || '.').replace(/\/$/, '');
    locale = locale || 'en';
    let d;
    try {
      d = await fetchJSON(base + '/strings' + (locale === 'en' ? '' : '.' + locale) + '.json');
    } catch (e) {
      d = await fetchJSON(base + '/strings.json');
    }
    const s = d.strings || {};
    const t = (key, fallback) => Object.prototype.hasOwnProperty.call(s, key) ? s[key] : fallback !== undefined ? fallback : key;
    return {
      locale: d.locale,
      name: d.name,
      strings: s,
      t
    };
  }
  const api = {
    load,
    loadCatalog,
    loadAudio,
    loadStrings,
    FILES
  };
  if (typeof module !== 'undefined' && module.exports) module.exports = api;
  root.QEContent = api;
})(typeof globalThis !== 'undefined' ? globalThis : this);
})(); } catch (e) { __ds_ns.__errors.push({ path: "assets/data/content.js", error: String((e && e.message) || e) }); }

// components/core/Button.jsx
try { (() => {
const {
  useState
} = React;
const V = {
  primary: {
    bg: 'var(--teal-deep)',
    bgH: 'var(--teal)',
    fg: 'var(--void-0)',
    bd: 'var(--teal)',
    glow: 'var(--glow-teal)'
  },
  secondary: {
    bg: 'var(--surface-raised)',
    bgH: 'var(--void-4)',
    fg: 'var(--teal)',
    bd: 'var(--border-bright)',
    glow: 'var(--glow-teal)'
  },
  ghost: {
    bg: 'transparent',
    bgH: 'rgba(45,212,207,.08)',
    fg: 'var(--fg-2)',
    bd: 'var(--border-panel)',
    glow: 'none'
  },
  danger: {
    bg: 'var(--surface-raised)',
    bgH: 'rgba(229,72,77,.15)',
    fg: 'var(--alert-red)',
    bd: 'var(--alert-red-deep)',
    glow: 'var(--glow-red)'
  }
};
function Button({
  variant = 'primary',
  size = 'md',
  disabled = false,
  children,
  onClick,
  style
}) {
  const v = V[variant] || V.primary;
  const [hov, setHov] = useState(false),
    [act, setAct] = useState(false);
  const pad = size === 'sm' ? '6px 14px' : size === 'lg' ? '14px 28px' : '10px 20px';
  return /*#__PURE__*/React.createElement("button", {
    onClick: disabled ? undefined : onClick,
    disabled: disabled,
    onMouseEnter: () => setHov(true),
    onMouseLeave: () => {
      setHov(false);
      setAct(false);
    },
    onMouseDown: () => setAct(true),
    onMouseUp: () => setAct(false),
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: size === 'sm' ? 10 : size === 'lg' ? 14 : 12,
      fontWeight: 700,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      padding: pad,
      cursor: disabled ? 'default' : 'pointer',
      borderRadius: 'var(--radius-1)',
      background: disabled ? 'var(--surface-panel)' : act ? v.bgH : hov ? v.bgH : v.bg,
      color: disabled ? 'var(--fg-disabled)' : variant === 'primary' && (hov || act) ? 'var(--void-0)' : v.fg,
      border: `1px solid ${disabled ? 'var(--border-panel)' : v.bd}`,
      boxShadow: disabled ? 'none' : hov || act ? v.glow : 'none',
      transition: 'all var(--dur-fast) var(--ease-ui)',
      minHeight: size === 'sm' ? 28 : 'var(--hit-target)',
      ...style
    }
  }, children);
}
Object.assign(__ds_scope, { Button });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Button.jsx", error: String((e && e.message) || e) }); }

// components/core/MenuList.jsx
try { (() => {
const {
  useState
} = React;
function MenuList({
  items = [],
  selected,
  onSelect,
  width = 180
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 'var(--sp-2)',
      width
    }
  }, items.map((it, i) => /*#__PURE__*/React.createElement(MenuItem, {
    key: i,
    label: it,
    active: selected === it,
    onClick: () => onSelect && onSelect(it)
  })));
}
function MenuItem({
  label,
  active,
  onClick
}) {
  const [hov, setHov] = useState(false);
  return /*#__PURE__*/React.createElement("button", {
    onClick: onClick,
    onMouseEnter: () => setHov(true),
    onMouseLeave: () => setHov(false),
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 12,
      fontWeight: 600,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      padding: '11px 16px',
      cursor: 'pointer',
      textAlign: 'center',
      borderRadius: 'var(--radius-1)',
      background: active ? 'rgba(45,212,207,.12)' : hov ? 'var(--surface-raised)' : 'var(--surface-panel)',
      color: active ? 'var(--teal)' : hov ? 'var(--fg-1)' : 'var(--fg-2)',
      border: `1px solid ${active ? 'var(--teal)' : hov ? 'var(--border-bright)' : 'var(--border-panel)'}`,
      boxShadow: active ? 'var(--glow-teal)' : 'none',
      transition: 'all var(--dur-fast) var(--ease-ui)'
    }
  }, label);
}
Object.assign(__ds_scope, { MenuList });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/MenuList.jsx", error: String((e && e.message) || e) }); }

// components/core/Tabs.jsx
try { (() => {
function Tabs({
  tabs = [],
  active,
  onChange
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 'var(--sp-1)',
      borderBottom: '1px solid var(--border-panel)'
    }
  }, tabs.map(t => /*#__PURE__*/React.createElement("button", {
    key: t,
    onClick: () => onChange && onChange(t),
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 600,
      letterSpacing: 'var(--tracking-wide)',
      textTransform: 'uppercase',
      padding: '8px 16px',
      cursor: 'pointer',
      border: 'none',
      borderBottom: `2px solid ${active === t ? 'var(--atom-gold)' : 'transparent'}`,
      background: active === t ? 'var(--surface-raised)' : 'transparent',
      color: active === t ? 'var(--atom-gold)' : 'var(--fg-3)',
      transition: 'all var(--dur-fast) var(--ease-ui)'
    }
  }, t)));
}
Object.assign(__ds_scope, { Tabs });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Tabs.jsx", error: String((e && e.message) || e) }); }

// components/dialogue/DialogueBox.jsx
try { (() => {
const KINDS = {
  dialogue: {
    bd: 'var(--electric-blue)',
    title: 'var(--electric-blue)'
  },
  system: {
    bd: 'var(--success-green-deep)',
    title: 'var(--success-green)'
  },
  warning: {
    bd: 'var(--alert-red)',
    title: 'var(--alert-red)'
  },
  confirm: {
    bd: 'var(--border-bright)',
    title: 'var(--fg-1)'
  }
};
function DialogueBox({
  kind = 'dialogue',
  speaker,
  portrait,
  children,
  actions,
  style
}) {
  const k = KINDS[kind] || KINDS.dialogue;
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 12,
      background: 'var(--surface-panel)',
      backgroundImage: 'var(--scanline)',
      border: `1px solid ${k.bd}`,
      borderRadius: 'var(--radius-1)',
      padding: 12,
      boxShadow: kind === 'warning' ? 'var(--glow-red)' : 'var(--shadow-panel)',
      ...style
    }
  }, portrait && /*#__PURE__*/React.createElement("img", {
    src: portrait,
    alt: speaker,
    style: {
      width: 64,
      height: 76,
      objectFit: 'cover',
      border: '1px solid var(--border-bright)',
      imageRendering: 'pixelated',
      flex: 'none'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      display: 'flex',
      flexDirection: 'column',
      gap: 6
    }
  }, speaker && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 700,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      color: k.title
    }
  }, speaker), /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 13,
      lineHeight: 1.5,
      color: 'var(--fg-1)'
    }
  }, children), actions && /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 8,
      marginTop: 4
    }
  }, actions)));
}
Object.assign(__ds_scope, { DialogueBox });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/dialogue/DialogueBox.jsx", error: String((e && e.message) || e) }); }

// components/equipment/StatMeter.jsx
try { (() => {
const RC = {
  common: 'var(--rarity-common)',
  uncommon: 'var(--rarity-uncommon)',
  rare: 'var(--rarity-rare)',
  epic: 'var(--rarity-epic)',
  legendary: 'var(--rarity-legendary)',
  mythic: 'var(--rarity-mythic)',
  exotic: 'var(--rarity-exotic)'
};
/** Labeled percentage meter — augmentation/ship attribute bars (Equipment §2, Starship stats). */
function StatMeter({
  label,
  value = 0,
  color = 'var(--teal)',
  showValue = true,
  width,
  style
}) {
  const pct = Math.max(0, Math.min(100, value));
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--sp-2)',
      width,
      ...style
    }
  }, label && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 9,
      fontWeight: 600,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      color: 'var(--text-muted)',
      width: 78,
      flex: 'none'
    }
  }, label), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      height: 8,
      background: 'var(--bar-track)',
      border: '1px solid var(--border-panel)',
      position: 'relative'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      width: pct + '%',
      height: '100%',
      background: color,
      boxShadow: `0 0 6px ${color}`,
      transition: 'width var(--dur-base) var(--ease-ui)'
    }
  })), showValue && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-ui)',
      fontSize: 10,
      color: 'var(--text-body)',
      flex: 'none',
      minWidth: 34,
      textAlign: 'right'
    }
  }, pct, "%"));
}
/** Insertable mod chip / augmentation — rarity-framed icon + name + effect line (Equipment §4). */
function ModChip({
  name,
  effect,
  rarity = 'common',
  icon,
  image,
  style
}) {
  const c = RC[rarity] || RC.common;
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--sp-3)',
      padding: 'var(--sp-2) var(--sp-3)',
      background: 'var(--surface-panel)',
      border: `1px solid var(--border-panel)`,
      borderLeft: `3px solid ${c}`,
      borderRadius: 'var(--radius-1)',
      ...style
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 28,
      height: 28,
      flex: 'none',
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      border: `2px solid ${c}`,
      borderRadius: 4,
      background: 'var(--surface-inset)',
      color: c,
      fontSize: 14,
      overflow: 'hidden'
    }
  }, image ? /*#__PURE__*/React.createElement("img", {
    src: image,
    alt: "",
    style: {
      width: '100%',
      height: '100%',
      objectFit: 'contain',
      imageRendering: 'pixelated'
    }
  }) : icon || '◆'), /*#__PURE__*/React.createElement("div", {
    style: {
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 700,
      letterSpacing: '.03em',
      textTransform: 'uppercase',
      color: c
    }
  }, name), effect && /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 11,
      color: 'var(--text-muted)',
      lineHeight: 1.35
    }
  }, effect)));
}
Object.assign(__ds_scope, { StatMeter, ModChip });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/equipment/StatMeter.jsx", error: String((e && e.message) || e) }); }

// components/hud/Notification.jsx
try { (() => {
function Notification({
  items = [],
  style
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 6,
      width: 220,
      ...style
    }
  }, items.map((n, i) => /*#__PURE__*/React.createElement("div", {
    key: i,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 8,
      background: 'var(--surface-raised)',
      border: '1px solid var(--border-bright)',
      borderLeft: '3px solid var(--teal)',
      padding: '7px 10px',
      fontSize: 11,
      color: 'var(--fg-1)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--teal)',
      fontSize: 12
    }
  }, "\u25C6"), n)));
}
function LootPopup({
  items = [],
  style
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      background: 'var(--surface-raised)',
      border: '1px solid var(--quantum-purple-dim)',
      boxShadow: 'var(--glow-purple)',
      padding: '8px 12px',
      width: 200,
      display: 'flex',
      flexDirection: 'column',
      gap: 6,
      ...style
    }
  }, items.map((it, i) => /*#__PURE__*/React.createElement("div", {
    key: i,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 8,
      fontSize: 11
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      color: it.color || 'var(--quantum-purple)'
    }
  }, "\u2726"), /*#__PURE__*/React.createElement("span", {
    style: {
      color: it.color || 'var(--fg-1)'
    }
  }, it.name), /*#__PURE__*/React.createElement("span", {
    style: {
      marginLeft: 'auto',
      color: 'var(--text-muted)'
    }
  }, "x ", it.count ?? 1))));
}
Object.assign(__ds_scope, { Notification, LootPopup });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/hud/Notification.jsx", error: String((e && e.message) || e) }); }

// components/hud/Panel.jsx
try { (() => {
function Panel({
  title,
  accent = 'var(--teal)',
  corners = true,
  children,
  style
}) {
  const tick = {
    position: 'absolute',
    width: 8,
    height: 8,
    borderColor: accent,
    borderStyle: 'solid'
  };
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'relative',
      background: 'var(--surface-panel)',
      backgroundImage: 'var(--scanline)',
      border: '1px solid var(--border-panel)',
      borderRadius: 'var(--radius-1)',
      boxShadow: 'var(--shadow-panel)',
      padding: 'var(--panel-pad)',
      ...style
    }
  }, corners && /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("span", {
    style: {
      ...tick,
      top: -1,
      left: -1,
      borderWidth: '2px 0 0 2px'
    }
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      ...tick,
      top: -1,
      right: -1,
      borderWidth: '2px 2px 0 0'
    }
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      ...tick,
      bottom: -1,
      left: -1,
      borderWidth: '0 0 2px 2px'
    }
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      ...tick,
      bottom: -1,
      right: -1,
      borderWidth: '0 2px 2px 0'
    }
  })), title && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 600,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      color: accent,
      marginBottom: 'var(--sp-3)'
    }
  }, title), children);
}
Object.assign(__ds_scope, { Panel });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/hud/Panel.jsx", error: String((e && e.message) || e) }); }

// components/hud/StatBar.jsx
try { (() => {
const K = {
  hp: ['var(--bar-hp)', 'var(--bar-hp-hi)'],
  ep: ['var(--bar-ep)', 'var(--bar-ep-hi)'],
  xp: ['var(--bar-xp)', 'var(--bar-xp-hi)'],
  boss: ['var(--quantum-purple-deep)', 'var(--quantum-purple)']
};
function StatBar({
  kind = 'hp',
  value = 100,
  max = 100,
  label,
  showValue = true,
  height = 12,
  style
}) {
  const [c1, c2] = K[kind] || K.hp;
  const pct = Math.max(0, Math.min(100, value / max * 100));
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--sp-2)',
      ...style
    }
  }, label && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 10,
      fontWeight: 600,
      letterSpacing: 'var(--tracking-wide)',
      color: c2,
      width: 24,
      flex: 'none'
    }
  }, label), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      height,
      background: 'var(--bar-track)',
      border: '1px solid var(--border-panel)',
      position: 'relative'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      width: pct + '%',
      height: '100%',
      background: `linear-gradient(90deg,${c1},${c2})`,
      transition: 'width var(--dur-base) var(--ease-ui)'
    }
  })), showValue && /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 10,
      color: 'var(--text-muted)',
      flex: 'none',
      minWidth: 52,
      textAlign: 'right'
    }
  }, value, "/", max));
}
Object.assign(__ds_scope, { StatBar });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/hud/StatBar.jsx", error: String((e && e.message) || e) }); }

// components/inventory/ItemSlot.jsx
try { (() => {
const RC = {
  common: 'var(--rarity-common)',
  uncommon: 'var(--rarity-uncommon)',
  rare: 'var(--rarity-rare)',
  epic: 'var(--rarity-epic)',
  legendary: 'var(--rarity-legendary)',
  mythic: 'var(--rarity-mythic)',
  exotic: 'var(--rarity-exotic)'
};
const RG = {
  epic: 'var(--glow-purple)',
  legendary: 'var(--glow-gold)',
  mythic: 'var(--glow-red)',
  exotic: 'var(--glow-teal)'
};
const BADGE = {
  new: {
    txt: 'NEW',
    bg: 'var(--atom-gold)',
    fg: '#1a1204'
  },
  favorite: {
    txt: '★',
    bg: 'transparent',
    fg: 'var(--atom-gold)'
  },
  equipped: {
    txt: 'E',
    bg: 'var(--electric-blue)',
    fg: '#fff'
  },
  locked: {
    txt: '🔒',
    bg: 'transparent',
    fg: 'var(--text-muted)'
  }
};
function ItemSlot({
  rarity = 'common',
  icon,
  image,
  count,
  quality,
  badge,
  selected = false,
  size = 48,
  empty = false,
  onClick,
  style
}) {
  const c = RC[rarity] || RC.common;
  const b = BADGE[badge];
  return /*#__PURE__*/React.createElement("div", {
    onClick: onClick,
    style: {
      position: 'relative',
      width: size,
      height: size,
      flex: 'none',
      cursor: onClick ? 'pointer' : 'default',
      background: 'var(--surface-inset)',
      border: `2px solid ${empty ? 'var(--border-panel)' : c}`,
      borderRadius: 'var(--radius-1)',
      boxShadow: selected ? 'var(--glow-teal)' : RG[rarity] && !empty ? RG[rarity] : 'none',
      outline: selected ? '1px solid var(--teal)' : 'none',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      ...style
    }
  }, image ? /*#__PURE__*/React.createElement("img", {
    src: image,
    alt: "",
    style: {
      width: '100%',
      height: '100%',
      objectFit: 'contain',
      imageRendering: 'pixelated'
    }
  }) : !empty && /*#__PURE__*/React.createElement("span", {
    style: {
      color: c,
      fontSize: size * .42,
      lineHeight: 1
    }
  }, icon || '◆'), count != null && /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'absolute',
      right: 2,
      bottom: 0,
      fontSize: 10,
      fontWeight: 700,
      color: 'var(--fg-1)',
      textShadow: '0 1px 2px #000'
    }
  }, count), quality && /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'absolute',
      left: 2,
      bottom: 0,
      fontSize: 9,
      fontWeight: 700,
      color: 'var(--success-green)',
      textShadow: '0 1px 2px #000'
    }
  }, quality), b && (b.bg === 'transparent' ? /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'absolute',
      top: 1,
      right: 2,
      fontSize: size * .24,
      lineHeight: 1,
      color: b.fg,
      textShadow: '0 1px 2px #000'
    }
  }, b.txt) : /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'absolute',
      top: -6,
      left: -4,
      fontFamily: 'var(--font-display)',
      fontSize: 8,
      fontWeight: 800,
      letterSpacing: '.03em',
      color: b.fg,
      background: b.bg,
      padding: '1px 4px',
      borderRadius: 2,
      boxShadow: '0 1px 3px #000'
    }
  }, b.txt)));
}
function RarityTag({
  rarity = 'common',
  children,
  style
}) {
  const c = RC[rarity] || RC.common;
  return /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 9,
      fontWeight: 700,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      color: c,
      border: `1px solid ${c}`,
      padding: '2px 8px',
      borderRadius: 'var(--radius-1)',
      ...style
    }
  }, children || rarity);
}
Object.assign(__ds_scope, { ItemSlot, RarityTag });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/inventory/ItemSlot.jsx", error: String((e && e.message) || e) }); }

// components/quantum/ElementCard.jsx
try { (() => {
const EC = {
  quantium: 'var(--elem-quantium)',
  voidium: 'var(--elem-voidium)',
  neutronite: 'var(--elem-neutronite)',
  singularium: 'var(--elem-singularium)',
  phasium: 'var(--elem-phasium)',
  entropium: 'var(--elem-entropium)',
  aeon: 'var(--elem-aeon)'
};
/** Quantum element crystal card — faceted gem glyph in element color + rarity/source/usage (Starships & Elements §8). */
function ElementCard({
  name,
  element = 'quantium',
  rarity,
  source,
  usage,
  description,
  width = 210,
  style
}) {
  const c = EC[element] || EC.quantium;
  return /*#__PURE__*/React.createElement("div", {
    style: {
      width,
      padding: 'var(--sp-3)',
      background: 'var(--surface-panel)',
      border: '1px solid var(--border-panel)',
      borderRadius: 'var(--radius-1)',
      boxShadow: 'var(--shadow-panel)',
      display: 'flex',
      gap: 'var(--sp-3)',
      ...style
    }
  }, /*#__PURE__*/React.createElement("svg", {
    width: "34",
    height: "42",
    viewBox: "0 0 34 42",
    style: {
      flex: 'none',
      filter: `drop-shadow(0 0 6px ${c})`
    }
  }, /*#__PURE__*/React.createElement("polygon", {
    points: "17,1 33,13 27,41 7,41 1,13",
    fill: c,
    opacity: "0.28"
  }), /*#__PURE__*/React.createElement("polygon", {
    points: "17,1 33,13 17,20 1,13",
    fill: c,
    opacity: "0.7"
  }), /*#__PURE__*/React.createElement("polygon", {
    points: "1,13 17,20 7,41",
    fill: c,
    opacity: "0.45"
  }), /*#__PURE__*/React.createElement("polygon", {
    points: "33,13 17,20 27,41",
    fill: c,
    opacity: "0.55"
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      minWidth: 0,
      flex: 1
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 12,
      fontWeight: 800,
      letterSpacing: '.04em',
      textTransform: 'uppercase',
      color: c
    }
  }, name), description && /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 10.5,
      color: 'var(--text-muted)',
      lineHeight: 1.35,
      margin: '3px 0 6px'
    }
  }, description), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 2,
      fontSize: 9,
      letterSpacing: '.04em'
    }
  }, rarity && /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--text-faint)',
      textTransform: 'uppercase'
    }
  }, "Rarity "), /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--fg-2)'
    }
  }, rarity)), source && /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--text-faint)',
      textTransform: 'uppercase'
    }
  }, "Source "), /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--fg-2)'
    }
  }, source)), usage && /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--text-faint)',
      textTransform: 'uppercase'
    }
  }, "Usage "), /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--fg-2)'
    }
  }, usage)))));
}
const HZ = {
  safe: {
    c: 'var(--hazard-safe)',
    label: 'Safe'
  },
  caution: {
    c: 'var(--hazard-caution)',
    label: 'Caution'
  },
  danger: {
    c: 'var(--hazard-danger)',
    label: 'Danger'
  },
  extreme: {
    c: 'var(--hazard-extreme)',
    label: 'Extreme'
  },
  unknown: {
    c: 'var(--hazard-unknown)',
    label: 'Unknown'
  }
};
/** Quantum-field hazard level badge — diamond glyph + level label (Starships & Elements §10). */
function HazardBadge({
  level = 'safe',
  note,
  size = 16,
  style
}) {
  const h = HZ[level] || HZ.safe;
  return /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 7,
      ...style
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: size,
      height: size,
      flex: 'none',
      transform: 'rotate(45deg)',
      border: `2px solid ${h.c}`,
      background: 'var(--surface-inset)',
      boxShadow: `0 0 6px ${h.c}`
    }
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      lineHeight: 1.15
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 10,
      fontWeight: 700,
      letterSpacing: '.06em',
      textTransform: 'uppercase',
      color: h.c,
      display: 'block'
    }
  }, h.label), note && /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 10,
      color: 'var(--text-faint)'
    }
  }, note)));
}
Object.assign(__ds_scope, { ElementCard, HazardBadge });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/quantum/ElementCard.jsx", error: String((e && e.message) || e) }); }

// components/quests/ObjectiveList.jsx
try { (() => {
function ObjectiveList({
  title,
  items = [],
  style
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      background: 'var(--surface-panel)',
      border: '1px solid var(--border-panel)',
      borderRadius: 'var(--radius-1)',
      ...style
    }
  }, title && /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '8px 12px',
      borderBottom: '1px solid var(--border-panel)',
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 700,
      letterSpacing: 'var(--tracking-wide)',
      textTransform: 'uppercase',
      color: 'var(--fg-1)'
    }
  }, title), /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '6px 0'
    }
  }, items.map((o, i) => /*#__PURE__*/React.createElement("div", {
    key: i,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 10,
      padding: '6px 12px',
      fontSize: 12,
      color: o.done ? 'var(--text-faint)' : 'var(--fg-1)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      color: o.done ? 'var(--success-green)' : 'var(--teal)',
      fontSize: 11,
      flex: 'none'
    }
  }, o.done ? '✔' : '◆'), /*#__PURE__*/React.createElement("span", {
    style: {
      flex: 1,
      textDecoration: o.done ? 'line-through' : 'none'
    }
  }, o.text), o.count && /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--text-muted)',
      fontSize: 11
    }
  }, o.count)))));
}
Object.assign(__ds_scope, { ObjectiveList });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/quests/ObjectiveList.jsx", error: String((e && e.message) || e) }); }

// components/quests/QuestCard.jsx
try { (() => {
const TIERS = {
  main: {
    c: 'var(--quest-main)',
    label: 'MAIN STORY'
  },
  side: {
    c: 'var(--quest-side)',
    label: 'SIDE QUEST'
  },
  faction: {
    c: 'var(--quest-faction)',
    label: 'FACTION QUEST'
  },
  world: {
    c: 'var(--quest-world)',
    label: 'WORLD EVENT'
  },
  daily: {
    c: 'var(--quest-daily)',
    label: 'DAILY QUEST'
  },
  hidden: {
    c: 'var(--quest-hidden)',
    label: 'HIDDEN QUEST'
  }
};
function QuestCard({
  tier = 'main',
  title,
  description,
  objective,
  progress,
  progressMax,
  level,
  image,
  footer,
  width = 200,
  style
}) {
  const t = TIERS[tier] || TIERS.main;
  return /*#__PURE__*/React.createElement("div", {
    style: {
      width,
      background: 'var(--surface-panel)',
      border: `1px solid ${t.c}`,
      borderRadius: 'var(--radius-1)',
      overflow: 'hidden',
      boxShadow: 'var(--shadow-panel)',
      display: 'flex',
      flexDirection: 'column',
      ...style
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      background: t.c,
      color: 'var(--void-0)',
      fontFamily: 'var(--font-display)',
      fontSize: 9,
      fontWeight: 700,
      letterSpacing: 'var(--tracking-caps)',
      textAlign: 'center',
      padding: '5px 0'
    }
  }, t.label), image && /*#__PURE__*/React.createElement("img", {
    src: image,
    alt: "",
    style: {
      width: '100%',
      height: 86,
      objectFit: 'cover',
      imageRendering: 'pixelated'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      padding: 10,
      display: 'flex',
      flexDirection: 'column',
      gap: 6,
      flex: 1
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 12,
      fontWeight: 700,
      color: 'var(--fg-1)'
    }
  }, title), description && /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 11,
      color: 'var(--text-muted)',
      lineHeight: 1.45
    }
  }, description), objective && /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 11,
      color: 'var(--fg-2)'
    }
  }, "Objective: ", objective), progressMax != null && /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 6
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      height: 6,
      background: 'var(--bar-track)',
      border: '1px solid var(--border-panel)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      width: progress / progressMax * 100 + '%',
      height: '100%',
      background: t.c
    }
  })), /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 10,
      color: 'var(--text-muted)'
    }
  }, progress, "/", progressMax)), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 'auto',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 10,
      color: 'var(--text-faint)'
    }
  }, footer), level && /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 10,
      color: t.c
    }
  }, "Lv. ", level))));
}
Object.assign(__ds_scope, { QuestCard });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/quests/QuestCard.jsx", error: String((e && e.message) || e) }); }

// components/quests/QuestRewards.jsx
try { (() => {
const RC = {
  common: 'var(--rarity-common)',
  uncommon: 'var(--rarity-uncommon)',
  rare: 'var(--rarity-rare)',
  epic: 'var(--rarity-epic)',
  legendary: 'var(--rarity-legendary)',
  mythic: 'var(--rarity-mythic)',
  exotic: 'var(--rarity-exotic)'
};
function Pill({
  glyph,
  color,
  value,
  label
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 7
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 22,
      height: 22,
      flex: 'none',
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      color: color,
      border: `1.5px solid ${color}`,
      borderRadius: 4,
      fontSize: 11,
      fontWeight: 700
    }
  }, glyph), /*#__PURE__*/React.createElement("div", {
    style: {
      lineHeight: 1.15
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-ui)',
      fontSize: 14,
      fontWeight: 700,
      color: 'var(--fg-1)'
    }
  }, value), /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 9,
      letterSpacing: '.08em',
      textTransform: 'uppercase',
      color: 'var(--text-faint)'
    }
  }, label)));
}
function QuestRewards({
  xp,
  reputation,
  currency,
  items = [],
  choice,
  style
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 'var(--sp-4)',
      ...style
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 'var(--sp-5)',
      flexWrap: 'wrap'
    }
  }, xp != null && /*#__PURE__*/React.createElement(Pill, {
    glyph: "XP",
    color: "var(--electric-blue)",
    value: xp,
    label: "Experience"
  }), reputation != null && /*#__PURE__*/React.createElement(Pill, {
    glyph: "\u25C6",
    color: "var(--quantum-purple)",
    value: reputation,
    label: "Reputation"
  }), currency != null && /*#__PURE__*/React.createElement(Pill, {
    glyph: "\u25C9",
    color: "var(--atom-gold)",
    value: currency,
    label: "Credits"
  })), items.length > 0 && /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 9,
      letterSpacing: '.1em',
      textTransform: 'uppercase',
      color: 'var(--text-faint)',
      marginBottom: 6
    }
  }, "Items"), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 8
    }
  }, items.map((it, i) => /*#__PURE__*/React.createElement("span", {
    key: i,
    title: it.name,
    style: {
      width: 36,
      height: 36,
      borderRadius: 4,
      border: `2px solid ${RC[it.rarity] || RC.common}`,
      background: 'var(--surface-inset)',
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      overflow: 'hidden'
    }
  }, it.image ? /*#__PURE__*/React.createElement("img", {
    src: it.image,
    alt: "",
    style: {
      width: '100%',
      height: '100%',
      objectFit: 'contain',
      imageRendering: 'pixelated'
    }
  }) : /*#__PURE__*/React.createElement("span", {
    style: {
      color: RC[it.rarity] || RC.common
    }
  }, "\u25C6"))))), choice && choice.length > 0 && /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 9,
      letterSpacing: '.1em',
      textTransform: 'uppercase',
      color: 'var(--atom-gold)',
      marginBottom: 6
    }
  }, "Choose One"), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 10,
      alignItems: 'center'
    }
  }, choice.map((it, i) => /*#__PURE__*/React.createElement(React.Fragment, {
    key: i
  }, i > 0 && /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 10,
      color: 'var(--text-faint)'
    }
  }, "OR"), /*#__PURE__*/React.createElement("span", {
    title: it.name,
    style: {
      width: 40,
      height: 40,
      borderRadius: 4,
      border: `2px solid ${RC[it.rarity] || RC.legendary}`,
      background: 'var(--surface-inset)',
      boxShadow: 'var(--glow-gold)',
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      overflow: 'hidden'
    }
  }, it.image ? /*#__PURE__*/React.createElement("img", {
    src: it.image,
    alt: "",
    style: {
      width: '100%',
      height: '100%',
      objectFit: 'contain',
      imageRendering: 'pixelated'
    }
  }) : /*#__PURE__*/React.createElement("span", {
    style: {
      color: RC[it.rarity] || RC.legendary
    }
  }, "\u25C6")))))));
}
Object.assign(__ds_scope, { QuestRewards });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/quests/QuestRewards.jsx", error: String((e && e.message) || e) }); }

// components/quests/QuestTracker.jsx
try { (() => {
const TIER = {
  main: 'var(--quest-main)',
  side: 'var(--quest-side)',
  faction: 'var(--quest-faction)',
  world: 'var(--quest-world)',
  daily: 'var(--quest-daily)',
  hidden: 'var(--quest-hidden)'
};
const STATES = {
  available: {
    g: '!',
    c: 'var(--teal)',
    label: 'Available'
  },
  accepted: {
    g: '○',
    c: 'var(--electric-blue)',
    label: 'Accepted'
  },
  'in-progress': {
    g: '◎',
    c: 'var(--atom-gold)',
    label: 'In Progress'
  },
  ready: {
    g: '!',
    c: 'var(--success-green)',
    label: 'Ready to Turn In'
  },
  completed: {
    g: '✔',
    c: 'var(--success-green)',
    label: 'Completed'
  },
  failed: {
    g: '✕',
    c: 'var(--alert-red)',
    label: 'Failed'
  },
  abandoned: {
    g: '✕',
    c: 'var(--text-faint)',
    label: 'Abandoned'
  },
  tracked: {
    g: '◆',
    c: 'var(--teal)',
    label: 'Tracked'
  },
  untracked: {
    g: '◇',
    c: 'var(--text-faint)',
    label: 'Untracked'
  }
};
function QuestStatus({
  state = 'available',
  showLabel = true,
  size = 16,
  style
}) {
  const s = STATES[state] || STATES.available;
  return /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 6,
      ...style
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: size,
      height: size,
      flex: 'none',
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      border: `1.5px solid ${s.c}`,
      borderRadius: '50%',
      color: s.c,
      fontSize: size * .6,
      lineHeight: 1
    }
  }, s.g), showLabel && /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 12,
      color: 'var(--text-body)'
    }
  }, s.label));
}
function QuestTracker({
  title = 'Tracked Quests',
  quests = [],
  width = 248,
  style
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      width,
      background: 'var(--surface-panel)',
      backgroundImage: 'var(--scanline)',
      border: '1px solid var(--border-panel)',
      borderRadius: 'var(--radius-1)',
      boxShadow: 'var(--shadow-panel)',
      overflow: 'hidden',
      ...style
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '7px 12px',
      borderBottom: '1px solid var(--border-panel)',
      fontFamily: 'var(--font-display)',
      fontSize: 10,
      fontWeight: 700,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      color: 'var(--text-section)'
    }
  }, title), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column'
    }
  }, quests.map((q, i) => {
    const c = TIER[q.tier] || TIER.main;
    return /*#__PURE__*/React.createElement("div", {
      key: i,
      style: {
        display: 'flex',
        gap: 9,
        padding: '9px 12px',
        borderLeft: `3px solid ${c}`,
        borderBottom: i < quests.length - 1 ? '1px solid var(--border-panel)' : 'none',
        background: q.active ? 'var(--surface-raised)' : 'transparent'
      }
    }, /*#__PURE__*/React.createElement("span", {
      style: {
        color: c,
        fontSize: 12,
        lineHeight: 1.3,
        flex: 'none'
      }
    }, "\u25C6"), /*#__PURE__*/React.createElement("div", {
      style: {
        flex: 1,
        minWidth: 0
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-display)',
        fontSize: 12,
        fontWeight: 600,
        color: q.active ? 'var(--fg-1)' : 'var(--text-body)',
        whiteSpace: 'nowrap',
        overflow: 'hidden',
        textOverflow: 'ellipsis'
      }
    }, q.title), /*#__PURE__*/React.createElement("div", {
      style: {
        fontSize: 11,
        color: q.timer ? 'var(--atom-gold)' : 'var(--text-muted)',
        marginTop: 2
      }
    }, q.timer ? 'Ends in ' + q.timer : q.sub)));
  })));
}
Object.assign(__ds_scope, { QuestStatus, QuestTracker });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/quests/QuestTracker.jsx", error: String((e && e.message) || e) }); }

// components/system/LoadingScreen.jsx
try { (() => {
function LoadingScreen({
  title = 'QUANTUM EFFECT',
  progress = 72,
  status = 'Loading…',
  tip,
  width = 440,
  style
}) {
  const pct = Math.max(0, Math.min(100, progress));
  return /*#__PURE__*/React.createElement("div", {
    style: {
      width,
      padding: 'var(--sp-6) var(--sp-5)',
      background: 'var(--surface-page)',
      backgroundImage: 'var(--scanline)',
      border: '1px solid var(--border-panel)',
      borderRadius: 'var(--radius-1)',
      boxShadow: 'var(--shadow-panel)',
      display: 'flex',
      flexDirection: 'column',
      gap: 'var(--sp-4)',
      ...style
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 34,
      fontWeight: 800,
      letterSpacing: 'var(--tracking-wide)',
      lineHeight: 1,
      background: 'linear-gradient(180deg,var(--atom-gold),var(--atom-gold-deep))',
      WebkitBackgroundClip: 'text',
      WebkitTextFillColor: 'transparent',
      backgroundClip: 'text'
    }
  }, title), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 'var(--sp-2)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      height: 8,
      background: 'var(--bar-track)',
      border: '1px solid var(--border-panel)',
      position: 'relative',
      overflow: 'hidden'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      width: pct + '%',
      height: '100%',
      background: 'linear-gradient(90deg,var(--electric-blue),var(--teal))',
      boxShadow: '0 0 8px var(--teal)',
      transition: 'width var(--dur-slow) var(--ease-ui)'
    }
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'space-between',
      fontSize: 11,
      color: 'var(--text-muted)'
    }
  }, /*#__PURE__*/React.createElement("span", null, status), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-ui)',
      color: 'var(--text-section)'
    }
  }, pct, "%"))), tip && /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 'var(--sp-1)',
      padding: 'var(--sp-3)',
      background: 'var(--surface-inset)',
      borderLeft: '2px solid var(--quantum-purple)',
      fontSize: 12,
      lineHeight: 1.45,
      color: 'var(--text-body)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 9,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      color: 'var(--quantum-purple)',
      display: 'block',
      marginBottom: 4
    }
  }, "Tip"), tip));
}
Object.assign(__ds_scope, { LoadingScreen });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/system/LoadingScreen.jsx", error: String((e && e.message) || e) }); }

// components/system/Minimap.jsx
try { (() => {
const MC = {
  player: 'var(--marker-player)',
  ally: 'var(--marker-ally)',
  enemy: 'var(--marker-enemy)',
  objective: 'var(--marker-objective)',
  poi: 'var(--marker-poi)'
};
function Blip({
  b,
  size
}) {
  const c = MC[b.kind] || MC.poi,
    x = b.x * size,
    y = b.y * size;
  if (b.kind === 'player') return /*#__PURE__*/React.createElement("polygon", {
    points: `${x},${y - 5} ${x + 4},${y + 4} ${x - 4},${y + 4}`,
    fill: c,
    stroke: "#000",
    strokeWidth: "0.5"
  });
  if (b.kind === 'objective') return /*#__PURE__*/React.createElement("g", null, /*#__PURE__*/React.createElement("circle", {
    cx: x,
    cy: y,
    r: "4",
    fill: "none",
    stroke: c,
    strokeWidth: "1.5"
  }), /*#__PURE__*/React.createElement("circle", {
    cx: x,
    cy: y,
    r: "1.5",
    fill: c
  }));
  return /*#__PURE__*/React.createElement("circle", {
    cx: x,
    cy: y,
    r: b.kind === 'enemy' ? 3 : 2.5,
    fill: c,
    stroke: "#000",
    strokeWidth: "0.4"
  });
}
function Minimap({
  shape = 'circular',
  size = 140,
  blips = [],
  fog = false,
  heading = 0,
  label,
  style
}) {
  const clip = shape === 'circular' ? 'circle(50%)' : 'none';
  const dots = blips.length ? blips : [{
    kind: 'player',
    x: 0.5,
    y: 0.5
  }, {
    kind: 'ally',
    x: 0.62,
    y: 0.4
  }, {
    kind: 'enemy',
    x: 0.35,
    y: 0.6
  }, {
    kind: 'objective',
    x: 0.7,
    y: 0.7
  }, {
    kind: 'poi',
    x: 0.28,
    y: 0.33
  }];
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'inline-flex',
      flexDirection: 'column',
      alignItems: 'center',
      gap: 'var(--sp-2)',
      ...style
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'relative',
      width: size,
      height: size
    }
  }, /*#__PURE__*/React.createElement("svg", {
    width: size,
    height: size,
    viewBox: `0 0 ${size} ${size}`,
    style: {
      display: 'block',
      clipPath: clip,
      background: 'var(--map-terrain)',
      border: `2px solid var(--border-bright)`,
      borderRadius: shape === 'circular' ? '50%' : 'var(--radius-1)',
      boxShadow: 'var(--shadow-panel)'
    }
  }, /*#__PURE__*/React.createElement("defs", null, /*#__PURE__*/React.createElement("radialGradient", {
    id: "fg",
    cx: "50%",
    cy: "50%",
    r: "55%"
  }, /*#__PURE__*/React.createElement("stop", {
    offset: "60%",
    stopColor: "transparent"
  }), /*#__PURE__*/React.createElement("stop", {
    offset: "100%",
    stopColor: "var(--map-fog)"
  }))), /*#__PURE__*/React.createElement("g", {
    opacity: "0.5",
    stroke: "var(--map-terrain-hi)",
    strokeWidth: "1"
  }, [...Array(5)].map((_, i) => /*#__PURE__*/React.createElement("line", {
    key: 'h' + i,
    x1: "0",
    y1: size / 5 * i,
    x2: size,
    y2: size / 5 * i
  })), [...Array(5)].map((_, i) => /*#__PURE__*/React.createElement("line", {
    key: 'v' + i,
    x1: size / 5 * i,
    y1: "0",
    x2: size / 5 * i,
    y2: size
  }))), /*#__PURE__*/React.createElement("path", {
    d: `M${size * 0.15},${size * 0.7} Q${size * 0.4},${size * 0.4} ${size * 0.85},${size * 0.55}`,
    fill: "none",
    stroke: "var(--map-terrain-hi)",
    strokeWidth: "6",
    opacity: "0.7"
  }), dots.map((b, i) => /*#__PURE__*/React.createElement(Blip, {
    key: i,
    b: b,
    size: size
  })), fog && /*#__PURE__*/React.createElement("rect", {
    width: size,
    height: size,
    fill: "url(#fg)"
  }), shape === 'circular' && /*#__PURE__*/React.createElement("circle", {
    cx: size / 2,
    cy: size / 2,
    r: size / 2 - 3,
    fill: "none",
    stroke: "var(--border-accent)",
    strokeWidth: "1",
    opacity: "0.4"
  })), shape === 'circular' && /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'absolute',
      top: 2,
      left: '50%',
      transform: 'translateX(-50%)',
      fontFamily: 'var(--font-display)',
      fontSize: 9,
      fontWeight: 700,
      color: 'var(--text-section)',
      textShadow: '0 1px 2px #000'
    }
  }, "N")), label && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 9,
      letterSpacing: 'var(--tracking-caps)',
      textTransform: 'uppercase',
      color: 'var(--text-muted)'
    }
  }, label));
}
Object.assign(__ds_scope, { Minimap });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/system/Minimap.jsx", error: String((e && e.message) || e) }); }

// ui_kits/game/DialogueScene.jsx
try { (() => {
function DialogueScene({
  onBack
}) {
  const {
    DialogueBox,
    Button
  } = window.DS;
  const lines = [{
    s: 'Lyra',
    p: '../../assets/portrait-lyra.png',
    k: 'dialogue',
    t: "The readings confirm it — the Core Relay is just ahead. Whatever the Void Seekers want with it, we can't let them reach it first."
  }, {
    s: 'Aurin',
    p: '../../assets/portrait-aurin.png',
    k: 'dialogue',
    t: "Then we move now. Every second we wait, the rift widens."
  }, {
    s: 'Lyra',
    p: '../../assets/portrait-lyra.png',
    k: 'dialogue',
    t: "Careful. Chronarch Prime knows we're coming. This choice will echo further than you know."
  }];
  const [i, setI] = React.useState(0);
  const [choice, setChoice] = React.useState(false);
  return /*#__PURE__*/React.createElement(window.Backdrop, {
    image: "../../assets/keyart-lab.png",
    dim: 0.55
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'flex-end',
      padding: 28,
      gap: 14
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 20,
      right: 20
    }
  }, /*#__PURE__*/React.createElement(Button, {
    variant: "ghost",
    size: "sm",
    onClick: onBack
  }, "Skip \u25B8")), /*#__PURE__*/React.createElement("div", {
    style: {
      maxWidth: 760,
      margin: '0 auto',
      width: '100%'
    }
  }, /*#__PURE__*/React.createElement(DialogueBox, {
    kind: lines[i].k,
    speaker: lines[i].s,
    portrait: lines[i].p
  }, lines[i].t), !choice ? /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'flex-end',
      marginTop: 10
    }
  }, /*#__PURE__*/React.createElement(Button, {
    size: "sm",
    onClick: () => {
      if (i < lines.length - 1) setI(i + 1);else setChoice(true);
    }
  }, i < lines.length - 1 ? 'Continue ▸' : 'Respond ▸')) : /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 8,
      marginTop: 12,
      maxWidth: 520
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 10,
      letterSpacing: '.14em',
      color: 'var(--teal)'
    }
  }, "CHOOSE YOUR RESPONSE"), /*#__PURE__*/React.createElement(Button, {
    variant: "secondary",
    size: "sm",
    onClick: onBack
  }, "\"I'll bring the relay online. No matter the cost.\""), /*#__PURE__*/React.createElement(Button, {
    variant: "secondary",
    size: "sm",
    onClick: onBack
  }, "\"And if the cost is too high?\""), /*#__PURE__*/React.createElement(Button, {
    variant: "ghost",
    size: "sm",
    onClick: onBack
  }, "[ Say nothing ]")))));
}
window.DialogueScene = DialogueScene;
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/game/DialogueScene.jsx", error: String((e && e.message) || e) }); }

// ui_kits/game/ExplorationHUD.jsx
try { (() => {
function ExplorationHUD({
  onOpenQuests,
  onOpenInventory,
  onTalk
}) {
  const {
    Panel,
    StatBar,
    ItemSlot,
    Notification,
    ObjectiveList,
    Button
  } = window.DS;
  return /*#__PURE__*/React.createElement(window.Backdrop, {
    image: "../../assets/keyart-neosolis.png",
    dim: 0.5
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      padding: 20
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 20,
      left: 20,
      width: 250
    }
  }, /*#__PURE__*/React.createElement(Panel, {
    title: "Aurin \xB7 Chronomancer \xB7 Lv. 23"
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 7
    }
  }, /*#__PURE__*/React.createElement(StatBar, {
    kind: "hp",
    label: "HP",
    value: 430,
    max: 520
  }), /*#__PURE__*/React.createElement(StatBar, {
    kind: "ep",
    label: "EP",
    value: 65,
    max: 80
  }), /*#__PURE__*/React.createElement(StatBar, {
    kind: "xp",
    label: "XP",
    value: 38,
    max: 100
  })))), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 20,
      right: 20,
      width: 180,
      display: 'flex',
      flexDirection: 'column',
      gap: 10
    }
  }, /*#__PURE__*/React.createElement(Panel, {
    accent: "var(--atom-gold)",
    corners: false,
    style: {
      padding: 0,
      overflow: 'hidden'
    }
  }, /*#__PURE__*/React.createElement("img", {
    src: "../../assets/keyart-neosolis.png",
    alt: "map",
    style: {
      width: '100%',
      height: 120,
      objectFit: 'cover',
      filter: 'hue-rotate(-10deg) saturate(1.2)',
      imageRendering: 'pixelated'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '6px 10px',
      fontFamily: 'var(--font-display)',
      fontSize: 9,
      letterSpacing: '.12em',
      color: 'var(--atom-gold)',
      borderTop: '1px solid var(--border-panel)'
    }
  }, "NEO-SOLIS \xB7 DISTRICT 7")), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'space-between',
      fontSize: 12,
      padding: '0 4px'
    }
  }, /*#__PURE__*/React.createElement(window.Coin, null, "12,450"), /*#__PURE__*/React.createElement(window.Shard, null, "318"))), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 230,
      right: 20,
      width: 230
    }
  }, /*#__PURE__*/React.createElement(ObjectiveList, {
    title: "The Core Relay",
    items: [{
      text: "Reach the Core Relay",
      count: "0/1"
    }, {
      text: "Override Security Node",
      count: "1/1",
      done: true
    }, {
      text: "Defend the terminal",
      count: "2/5"
    }]
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 230,
      left: 20
    }
  }, /*#__PURE__*/React.createElement(Notification, {
    items: ["New Quest: The Core Relay", "Discovered: District 7"]
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 20,
      left: '50%',
      transform: 'translateX(-50%)',
      display: 'flex',
      gap: 8,
      alignItems: 'center'
    }
  }, /*#__PURE__*/React.createElement(ItemSlot, {
    rarity: "epic",
    image: window.ICON + 'skill-fire.png',
    quality: "Q"
  }), /*#__PURE__*/React.createElement(ItemSlot, {
    rarity: "rare",
    image: window.ICON + 'skill-frost.png',
    quality: "W"
  }), /*#__PURE__*/React.createElement(ItemSlot, {
    rarity: "legendary",
    image: window.ICON + 'skill-nova.png',
    quality: "E"
  }), /*#__PURE__*/React.createElement(ItemSlot, {
    rarity: "uncommon",
    image: window.ICON + 'skill-slash.png',
    quality: "R"
  }), /*#__PURE__*/React.createElement(ItemSlot, {
    rarity: "common",
    image: window.ICON + 'potion-health.png',
    quality: "F"
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 20,
      right: 20,
      display: 'flex',
      gap: 8
    }
  }, /*#__PURE__*/React.createElement(Button, {
    variant: "secondary",
    size: "sm",
    onClick: onTalk
  }, "Talk"), /*#__PURE__*/React.createElement(Button, {
    variant: "secondary",
    size: "sm",
    onClick: onOpenQuests
  }, "Quests"), /*#__PURE__*/React.createElement(Button, {
    variant: "secondary",
    size: "sm",
    onClick: onOpenInventory
  }, "Inventory")), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 110,
      left: '50%',
      transform: 'translateX(-50%)',
      fontSize: 12,
      color: 'var(--teal)',
      letterSpacing: '.08em',
      textShadow: '0 0 8px #000'
    }
  }, "[ E ] Speak with Lyra")));
}
window.ExplorationHUD = ExplorationHUD;
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/game/ExplorationHUD.jsx", error: String((e && e.message) || e) }); }

// ui_kits/game/Inventory.jsx
try { (() => {
function Inventory({
  onBack
}) {
  const {
    Panel,
    ItemSlot,
    Tabs,
    RarityTag,
    Button,
    StatBar
  } = window.DS;
  const [tab, setTab] = React.useState('All');
  const [sel, setSel] = React.useState(0);
  const I = window.ICON;
  const items = [{
    r: 'legendary',
    q: '+15',
    n: 'Chrono Blade',
    t: 'Weapon · Sword',
    img: I + 'weapon-sword-legendary.png',
    d: 'Deals heavy damage. May distort time on critical hits.'
  }, {
    r: 'epic',
    c: 3,
    n: 'Quantum Fragment',
    t: 'Material',
    img: I + 'crystal-essence.png'
  }, {
    r: 'rare',
    n: 'Void Visor',
    t: 'Armor · Head',
    img: I + 'elem-void.png'
  }, {
    r: 'uncommon',
    c: 12,
    n: 'Solarite Shard',
    t: 'Material',
    img: I + 'gem-green.png'
  }, {
    r: 'common',
    c: 5,
    n: 'Ration Pack',
    t: 'Consumable',
    img: I + 'potion-health.png'
  }, {
    r: 'exotic',
    n: 'Rift Anchor',
    t: 'Relic',
    img: I + 'gem-blue.png'
  }, {
    r: 'mythic',
    n: 'Sin of Ambition',
    t: 'Relic',
    img: I + 'elem-quantum.png'
  }, {
    r: 'rare',
    n: 'Pulse Rifle',
    t: 'Weapon',
    img: I + 'weapon-sword-rare.png'
  }];
  const cur = items[sel];
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      background: 'var(--surface-page)',
      backgroundImage: 'var(--scanline)',
      padding: 28,
      display: 'flex',
      flexDirection: 'column',
      gap: 16
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between'
    }
  }, /*#__PURE__*/React.createElement("h1", {
    style: {
      fontSize: 22,
      color: 'var(--atom-gold)',
      margin: 0
    }
  }, "Inventory"), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 16,
      alignItems: 'center'
    }
  }, /*#__PURE__*/React.createElement(window.Coin, null, "12,450"), /*#__PURE__*/React.createElement(window.Shard, null, "318"), /*#__PURE__*/React.createElement(Button, {
    variant: "ghost",
    size: "sm",
    onClick: onBack
  }, "\u2190 Back"))), /*#__PURE__*/React.createElement(Tabs, {
    tabs: ["All", "Weapon", "Armor", "Material", "Relic", "Consumable"],
    active: tab,
    onChange: setTab
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 20,
      flex: 1,
      minHeight: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'grid',
      gridTemplateColumns: 'repeat(8,48px)',
      gap: 8,
      alignContent: 'flex-start'
    }
  }, items.map((it, i) => /*#__PURE__*/React.createElement(ItemSlot, {
    key: i,
    rarity: it.r,
    image: it.img,
    count: it.c,
    quality: it.q,
    selected: sel === i,
    onClick: () => setSel(i)
  })), Array.from({
    length: 24
  }).map((_, i) => /*#__PURE__*/React.createElement(ItemSlot, {
    key: 'e' + i,
    empty: true
  }))), /*#__PURE__*/React.createElement("div", {
    style: {
      width: 280,
      flex: 'none'
    }
  }, /*#__PURE__*/React.createElement(Panel, {
    title: "Item Detail",
    accent: `var(--rarity-${cur.r})`
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 12,
      alignItems: 'flex-start'
    }
  }, /*#__PURE__*/React.createElement(ItemSlot, {
    rarity: cur.r,
    image: cur.img,
    size: 64,
    quality: cur.q
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 6,
      flex: 1,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 13,
      lineHeight: 1.25,
      color: 'var(--fg-1)'
    }
  }, cur.n), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement(RarityTag, {
    rarity: cur.r
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 11,
      color: 'var(--text-muted)'
    }
  }, cur.t))), cur.d && /*#__PURE__*/React.createElement("p", {
    style: {
      fontSize: 12,
      color: 'var(--text-muted)',
      lineHeight: 1.5
    }
  }, cur.d), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 10,
      letterSpacing: '.12em',
      color: 'var(--text-faint)',
      margin: '8px 0 4px'
    }
  }, "STATS"), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 5
    }
  }, /*#__PURE__*/React.createElement(StatBar, {
    kind: "hp",
    label: "ATK",
    value: cur.r === 'legendary' ? 92 : 40,
    max: 100,
    showValue: false,
    height: 8
  }), /*#__PURE__*/React.createElement(StatBar, {
    kind: "ep",
    label: "MAG",
    value: cur.r === 'legendary' ? 70 : 30,
    max: 100,
    showValue: false,
    height: 8
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 8,
      marginTop: 14
    }
  }, /*#__PURE__*/React.createElement(Button, {
    size: "sm"
  }, "Equip"), /*#__PURE__*/React.createElement(Button, {
    variant: "ghost",
    size: "sm"
  }, "Drop"))))));
}
window.Inventory = Inventory;
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/game/Inventory.jsx", error: String((e && e.message) || e) }); }

// ui_kits/game/MainMenu.jsx
try { (() => {
function MainMenu({
  onPlay
}) {
  const {
    MenuList
  } = window.DS;
  const [sel, setSel] = React.useState('Continue');
  const items = ["Continue", "New Game", "Load Game", "Settings", "Credits", "Quit"];
  return /*#__PURE__*/React.createElement(window.Backdrop, {
    image: "../../assets/keyart-hero-city.png",
    dim: 0.62
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      height: '100%',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      paddingLeft: 80,
      gap: 32
    }
  }, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("img", {
    src: "../../assets/logo-gold.png",
    alt: "Quantum Effect",
    style: {
      height: 96
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 12,
      letterSpacing: '.3em',
      color: 'var(--teal)',
      marginTop: 12,
      textTransform: 'uppercase'
    }
  }, "The Quantum is Power \xB7 Power Corrupts \xB7 Choice Defines")), /*#__PURE__*/React.createElement("div", {
    onClick: e => {
      const t = e.target.textContent;
      if (t) {
        setSel(t);
        if (t === 'Continue' || t === 'New Game') onPlay && onPlay();
      }
    }
  }, /*#__PURE__*/React.createElement(MenuList, {
    items: items,
    selected: sel,
    width: 220
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      fontSize: 11,
      color: 'var(--text-faint)'
    }
  }, "SAVE 01 \xB7 Aurin \xB7 Lv. 23 \xB7 Neo-Solis \xB7 42:18 played")), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 20,
      right: 28,
      fontSize: 10,
      color: 'var(--text-faint)',
      letterSpacing: '.1em'
    }
  }, "QUANTUM EFFECT \xB7 BUILD 0.7.7 \xB7 \xA9 FIRST EARTH INTERACTIVE"));
}
window.MainMenu = MainMenu;
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/game/MainMenu.jsx", error: String((e && e.message) || e) }); }

// ui_kits/game/QuestLog.jsx
try { (() => {
function QuestLog({
  onBack
}) {
  const {
    Panel,
    QuestCard,
    Tabs,
    Button,
    ObjectiveList
  } = window.DS;
  const [tab, setTab] = React.useState('Main');
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      background: 'var(--surface-page)',
      backgroundImage: 'var(--scanline)',
      padding: 28,
      display: 'flex',
      flexDirection: 'column',
      gap: 16
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between'
    }
  }, /*#__PURE__*/React.createElement("h1", {
    style: {
      fontSize: 22,
      color: 'var(--atom-gold)',
      margin: 0
    }
  }, "Quest Log"), /*#__PURE__*/React.createElement(Button, {
    variant: "ghost",
    size: "sm",
    onClick: onBack
  }, "\u2190 Back")), /*#__PURE__*/React.createElement(Tabs, {
    tabs: ["Main", "Side", "Faction", "World", "Completed"],
    active: tab,
    onChange: setTab
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 16,
      flex: 1,
      minHeight: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 14,
      flexWrap: 'wrap',
      alignContent: 'flex-start'
    }
  }, /*#__PURE__*/React.createElement(QuestCard, {
    tier: "main",
    title: "The Core Relay",
    image: "../../assets/keyart-neosolis.png",
    description: "The relay's signal leads to the heart of Quantum.",
    objective: "Reach the Core Relay",
    progress: 0,
    progressMax: 1,
    level: "18+",
    footer: "Neo-Solis"
  }), /*#__PURE__*/React.createElement(QuestCard, {
    tier: "side",
    title: "Missing Convoy",
    image: "../../assets/keyart-wasteland.png",
    description: "A merchant convoy vanished in the Ashfields.",
    progress: 1,
    progressMax: 3,
    level: "16",
    footer: "Ashfields"
  }), /*#__PURE__*/React.createElement(QuestCard, {
    tier: "faction",
    title: "Echoes of the Void",
    image: "../../assets/keyart-lab.png",
    description: "The Void Seekers request your aid at the rift.",
    progress: 2,
    progressMax: 5,
    level: "20",
    footer: "Void Seekers"
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      width: 260,
      flex: 'none'
    }
  }, /*#__PURE__*/React.createElement(Panel, {
    title: "Tracked \xB7 The Core Relay",
    accent: "var(--quest-main)"
  }, /*#__PURE__*/React.createElement("p", {
    style: {
      fontSize: 12,
      color: 'var(--text-muted)',
      lineHeight: 1.5,
      marginTop: 0
    }
  }, "\"The Core Relay is our last line of defense. Get it online, and keep it safe. Quantum depends on you.\""), /*#__PURE__*/React.createElement(ObjectiveList, {
    items: [{
      text: "Reach the Core Relay",
      count: "0/1"
    }, {
      text: "Override Security Node",
      count: "1/1",
      done: true
    }, {
      text: "Defend the terminal",
      count: "2/5"
    }]
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 12,
      fontFamily: 'var(--font-display)',
      fontSize: 10,
      letterSpacing: '.12em',
      color: 'var(--text-faint)'
    }
  }, "REWARDS"), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 16,
      marginTop: 6,
      fontSize: 12
    }
  }, /*#__PURE__*/React.createElement(window.Coin, null, "2,500"), /*#__PURE__*/React.createElement(window.Shard, null, "40"), /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--rarity-epic)'
    }
  }, "\u25C6 Chrono Cell"))))));
}
window.QuestLog = QuestLog;
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/game/QuestLog.jsx", error: String((e && e.message) || e) }); }

// ui_kits/game/kit.jsx
try { (() => {
// Shared helpers for the Quantum Effect game UI kit. Exported to window for cross-<script> access.
const DS = window.QuantumEffectDesignSystem_2d830e;
function Backdrop({
  image,
  children,
  dim = 0.78
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      overflow: 'hidden'
    }
  }, /*#__PURE__*/React.createElement("img", {
    src: image,
    alt: "",
    style: {
      position: 'absolute',
      inset: 0,
      width: '100%',
      height: '100%',
      objectFit: 'cover',
      imageRendering: 'pixelated'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      background: `rgba(4,6,13,${dim})`,
      backgroundImage: 'var(--scanline)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'relative',
      height: '100%'
    }
  }, children));
}
const ICON = '../../assets/icons/';
function Coin({
  children
}) {
  return /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--atom-gold)',
      display: 'inline-flex',
      alignItems: 'center',
      gap: 5
    }
  }, /*#__PURE__*/React.createElement("img", {
    src: ICON + 'coin-gold.png',
    style: {
      width: 18,
      height: 18,
      imageRendering: 'pixelated'
    }
  }), children);
}
function Shard({
  children
}) {
  return /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--teal)',
      display: 'inline-flex',
      alignItems: 'center',
      gap: 5
    }
  }, /*#__PURE__*/React.createElement("img", {
    src: ICON + 'gem-blue.png',
    style: {
      width: 18,
      height: 18,
      imageRendering: 'pixelated'
    }
  }), children);
}
Object.assign(window, {
  DS,
  Backdrop,
  Coin,
  Shard,
  ICON
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/game/kit.jsx", error: String((e && e.message) || e) }); }

__ds_ns.Button = __ds_scope.Button;

__ds_ns.MenuList = __ds_scope.MenuList;

__ds_ns.Tabs = __ds_scope.Tabs;

__ds_ns.DialogueBox = __ds_scope.DialogueBox;

__ds_ns.StatMeter = __ds_scope.StatMeter;

__ds_ns.ModChip = __ds_scope.ModChip;

__ds_ns.Notification = __ds_scope.Notification;

__ds_ns.LootPopup = __ds_scope.LootPopup;

__ds_ns.Panel = __ds_scope.Panel;

__ds_ns.StatBar = __ds_scope.StatBar;

__ds_ns.ItemSlot = __ds_scope.ItemSlot;

__ds_ns.RarityTag = __ds_scope.RarityTag;

__ds_ns.ElementCard = __ds_scope.ElementCard;

__ds_ns.HazardBadge = __ds_scope.HazardBadge;

__ds_ns.ObjectiveList = __ds_scope.ObjectiveList;

__ds_ns.QuestCard = __ds_scope.QuestCard;

__ds_ns.QuestRewards = __ds_scope.QuestRewards;

__ds_ns.QuestStatus = __ds_scope.QuestStatus;

__ds_ns.QuestTracker = __ds_scope.QuestTracker;

__ds_ns.LoadingScreen = __ds_scope.LoadingScreen;

__ds_ns.Minimap = __ds_scope.Minimap;

})();
