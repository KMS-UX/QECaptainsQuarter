import React from 'react';
const KINDS={
dialogue:{bd:'var(--electric-blue)',title:'var(--electric-blue)'},
system:{bd:'var(--success-green-deep)',title:'var(--success-green)'},
warning:{bd:'var(--alert-red)',title:'var(--alert-red)'},
confirm:{bd:'var(--border-bright)',title:'var(--fg-1)'},
};
export function DialogueBox({kind='dialogue',speaker,portrait,children,actions,style}){
  const k=KINDS[kind]||KINDS.dialogue;
  return <div style={{display:'flex',gap:12,background:'var(--surface-panel)',backgroundImage:'var(--scanline)',border:`1px solid ${k.bd}`,borderRadius:'var(--radius-1)',padding:12,boxShadow:kind==='warning'?'var(--glow-red)':'var(--shadow-panel)',...style}}>
    {portrait&&<img src={portrait} alt={speaker} style={{width:64,height:76,objectFit:'cover',border:'1px solid var(--border-bright)',imageRendering:'pixelated',flex:'none'}}/>}
    <div style={{flex:1,display:'flex',flexDirection:'column',gap:6}}>
      {speaker&&<div style={{fontFamily:'var(--font-display)',fontSize:11,fontWeight:700,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',color:k.title}}>{speaker}</div>}
      <div style={{fontSize:13,lineHeight:1.5,color:'var(--fg-1)'}}>{children}</div>
      {actions&&<div style={{display:'flex',gap:8,marginTop:4}}>{actions}</div>}
    </div>
  </div>;
}
