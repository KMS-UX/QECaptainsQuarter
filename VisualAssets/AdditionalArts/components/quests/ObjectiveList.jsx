import React from 'react';
export function ObjectiveList({title,items=[],style}){
  return <div style={{background:'var(--surface-panel)',border:'1px solid var(--border-panel)',borderRadius:'var(--radius-1)',...style}}>
    {title&&<div style={{padding:'8px 12px',borderBottom:'1px solid var(--border-panel)',fontFamily:'var(--font-display)',fontSize:11,fontWeight:700,letterSpacing:'var(--tracking-wide)',textTransform:'uppercase',color:'var(--fg-1)'}}>{title}</div>}
    <div style={{padding:'6px 0'}}>
      {items.map((o,i)=><div key={i} style={{display:'flex',alignItems:'center',gap:10,padding:'6px 12px',fontSize:12,color:o.done?'var(--text-faint)':'var(--fg-1)'}}>
        <span style={{color:o.done?'var(--success-green)':'var(--teal)',fontSize:11,flex:'none'}}>{o.done?'✔':'◆'}</span>
        <span style={{flex:1,textDecoration:o.done?'line-through':'none'}}>{o.text}</span>
        {o.count&&<span style={{color:'var(--text-muted)',fontSize:11}}>{o.count}</span>}
      </div>)}
    </div>
  </div>;
}
