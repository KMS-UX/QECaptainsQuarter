import React from 'react';
const K={hp:['var(--bar-hp)','var(--bar-hp-hi)'],ep:['var(--bar-ep)','var(--bar-ep-hi)'],xp:['var(--bar-xp)','var(--bar-xp-hi)'],boss:['var(--quantum-purple-deep)','var(--quantum-purple)']};
export function StatBar({kind='hp',value=100,max=100,label,showValue=true,height=12,style}){
  const [c1,c2]=K[kind]||K.hp; const pct=Math.max(0,Math.min(100,value/max*100));
  return <div style={{display:'flex',alignItems:'center',gap:'var(--sp-2)',...style}}>
    {label&&<span style={{fontFamily:'var(--font-display)',fontSize:10,fontWeight:600,letterSpacing:'var(--tracking-wide)',color:c2,width:24,flex:'none'}}>{label}</span>}
    <div style={{flex:1,height,background:'var(--bar-track)',border:'1px solid var(--border-panel)',position:'relative'}}>
      <div style={{width:pct+'%',height:'100%',background:`linear-gradient(90deg,${c1},${c2})`,transition:'width var(--dur-base) var(--ease-ui)'}}></div>
    </div>
    {showValue&&<span style={{fontSize:10,color:'var(--text-muted)',flex:'none',minWidth:52,textAlign:'right'}}>{value}/{max}</span>}
  </div>;
}
