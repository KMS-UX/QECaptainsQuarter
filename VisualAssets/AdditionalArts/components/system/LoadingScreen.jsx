import React from 'react';
export function LoadingScreen({title='QUANTUM EFFECT',progress=72,status='Loading…',tip,width=440,style}){
  const pct=Math.max(0,Math.min(100,progress));
  return <div style={{width,padding:'var(--sp-6) var(--sp-5)',background:'var(--surface-page)',backgroundImage:'var(--scanline)',border:'1px solid var(--border-panel)',borderRadius:'var(--radius-1)',boxShadow:'var(--shadow-panel)',display:'flex',flexDirection:'column',gap:'var(--sp-4)',...style}}>
    <div style={{fontFamily:'var(--font-display)',fontSize:34,fontWeight:800,letterSpacing:'var(--tracking-wide)',lineHeight:1,background:'linear-gradient(180deg,var(--atom-gold),var(--atom-gold-deep))',WebkitBackgroundClip:'text',WebkitTextFillColor:'transparent',backgroundClip:'text'}}>{title}</div>
    <div style={{display:'flex',flexDirection:'column',gap:'var(--sp-2)'}}>
      <div style={{height:8,background:'var(--bar-track)',border:'1px solid var(--border-panel)',position:'relative',overflow:'hidden'}}>
        <div style={{width:pct+'%',height:'100%',background:'linear-gradient(90deg,var(--electric-blue),var(--teal))',boxShadow:'0 0 8px var(--teal)',transition:'width var(--dur-slow) var(--ease-ui)'}}></div>
      </div>
      <div style={{display:'flex',justifyContent:'space-between',fontSize:11,color:'var(--text-muted)'}}><span>{status}</span><span style={{fontFamily:'var(--font-ui)',color:'var(--text-section)'}}>{pct}%</span></div>
    </div>
    {tip&&<div style={{marginTop:'var(--sp-1)',padding:'var(--sp-3)',background:'var(--surface-inset)',borderLeft:'2px solid var(--quantum-purple)',fontSize:12,lineHeight:1.45,color:'var(--text-body)'}}>
      <span style={{fontFamily:'var(--font-display)',fontSize:9,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',color:'var(--quantum-purple)',display:'block',marginBottom:4}}>Tip</span>{tip}</div>}
  </div>;
}
