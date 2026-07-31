import React,{useState} from 'react';
const V={
primary:{bg:'var(--teal-deep)',bgH:'var(--teal)',fg:'var(--void-0)',bd:'var(--teal)',glow:'var(--glow-teal)'},
secondary:{bg:'var(--surface-raised)',bgH:'var(--void-4)',fg:'var(--teal)',bd:'var(--border-bright)',glow:'var(--glow-teal)'},
ghost:{bg:'transparent',bgH:'rgba(45,212,207,.08)',fg:'var(--fg-2)',bd:'var(--border-panel)',glow:'none'},
danger:{bg:'var(--surface-raised)',bgH:'rgba(229,72,77,.15)',fg:'var(--alert-red)',bd:'var(--alert-red-deep)',glow:'var(--glow-red)'},
};
export function Button({variant='primary',size='md',disabled=false,children,onClick,style}){
  const v=V[variant]||V.primary;
  const [hov,setHov]=useState(false),[act,setAct]=useState(false);
  const pad=size==='sm'?'6px 14px':size==='lg'?'14px 28px':'10px 20px';
  return <button onClick={disabled?undefined:onClick} disabled={disabled}
    onMouseEnter={()=>setHov(true)} onMouseLeave={()=>{setHov(false);setAct(false)}}
    onMouseDown={()=>setAct(true)} onMouseUp={()=>setAct(false)}
    style={{fontFamily:'var(--font-display)',fontSize:size==='sm'?10:size==='lg'?14:12,fontWeight:700,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',
    padding:pad,cursor:disabled?'default':'pointer',borderRadius:'var(--radius-1)',
    background:disabled?'var(--surface-panel)':act?v.bgH:hov?v.bgH:v.bg,
    color:disabled?'var(--fg-disabled)':variant==='primary'&&(hov||act)?'var(--void-0)':v.fg,
    border:`1px solid ${disabled?'var(--border-panel)':v.bd}`,
    boxShadow:disabled?'none':hov||act?v.glow:'none',
    transition:'all var(--dur-fast) var(--ease-ui)',minHeight:size==='sm'?28:'var(--hit-target)',...style}}>{children}</button>;
}
