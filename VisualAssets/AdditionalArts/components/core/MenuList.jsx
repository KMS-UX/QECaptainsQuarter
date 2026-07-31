import React,{useState} from 'react';
export function MenuList({items=[],selected,onSelect,width=180}){
  return <div style={{display:'flex',flexDirection:'column',gap:'var(--sp-2)',width}}>
    {items.map((it,i)=><MenuItem key={i} label={it} active={selected===it} onClick={()=>onSelect&&onSelect(it)}/>)}
  </div>;
}
function MenuItem({label,active,onClick}){
  const [hov,setHov]=useState(false);
  return <button onClick={onClick} onMouseEnter={()=>setHov(true)} onMouseLeave={()=>setHov(false)}
    style={{fontFamily:'var(--font-display)',fontSize:12,fontWeight:600,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',
    padding:'11px 16px',cursor:'pointer',textAlign:'center',borderRadius:'var(--radius-1)',
    background:active?'rgba(45,212,207,.12)':hov?'var(--surface-raised)':'var(--surface-panel)',
    color:active?'var(--teal)':hov?'var(--fg-1)':'var(--fg-2)',
    border:`1px solid ${active?'var(--teal)':hov?'var(--border-bright)':'var(--border-panel)'}`,
    boxShadow:active?'var(--glow-teal)':'none',transition:'all var(--dur-fast) var(--ease-ui)'}}>{label}</button>;
}
