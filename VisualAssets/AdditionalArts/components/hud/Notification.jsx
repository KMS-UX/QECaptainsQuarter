import React from 'react';
export function Notification({items=[],style}){
  return <div style={{display:'flex',flexDirection:'column',gap:6,width:220,...style}}>
    {items.map((n,i)=><div key={i} style={{display:'flex',alignItems:'center',gap:8,background:'var(--surface-raised)',border:'1px solid var(--border-bright)',borderLeft:'3px solid var(--teal)',padding:'7px 10px',fontSize:11,color:'var(--fg-1)'}}>
      <span style={{color:'var(--teal)',fontSize:12}}>◆</span>{n}
    </div>)}
  </div>;
}
export function LootPopup({items=[],style}){
  return <div style={{background:'var(--surface-raised)',border:'1px solid var(--quantum-purple-dim)',boxShadow:'var(--glow-purple)',padding:'8px 12px',width:200,display:'flex',flexDirection:'column',gap:6,...style}}>
    {items.map((it,i)=><div key={i} style={{display:'flex',alignItems:'center',gap:8,fontSize:11}}>
      <span style={{color:it.color||'var(--quantum-purple)'}}>✦</span>
      <span style={{color:it.color||'var(--fg-1)'}}>{it.name}</span>
      <span style={{marginLeft:'auto',color:'var(--text-muted)'}}>x {it.count??1}</span>
    </div>)}
  </div>;
}
