import React from 'react';
export function Tabs({tabs=[],active,onChange}){
  return <div style={{display:'flex',gap:'var(--sp-1)',borderBottom:'1px solid var(--border-panel)'}}>
    {tabs.map(t=><button key={t} onClick={()=>onChange&&onChange(t)}
      style={{fontFamily:'var(--font-display)',fontSize:11,fontWeight:600,letterSpacing:'var(--tracking-wide)',textTransform:'uppercase',
      padding:'8px 16px',cursor:'pointer',border:'none',borderBottom:`2px solid ${active===t?'var(--atom-gold)':'transparent'}`,
      background:active===t?'var(--surface-raised)':'transparent',
      color:active===t?'var(--atom-gold)':'var(--fg-3)',transition:'all var(--dur-fast) var(--ease-ui)'}}>{t}</button>)}
  </div>;
}
