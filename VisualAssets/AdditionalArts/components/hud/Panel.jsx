import React from 'react';
export function Panel({title,accent='var(--teal)',corners=true,children,style}){
  const tick={position:'absolute',width:8,height:8,borderColor:accent,borderStyle:'solid'};
  return <div style={{position:'relative',background:'var(--surface-panel)',backgroundImage:'var(--scanline)',border:'1px solid var(--border-panel)',borderRadius:'var(--radius-1)',boxShadow:'var(--shadow-panel)',padding:'var(--panel-pad)',...style}}>
    {corners&&<><span style={{...tick,top:-1,left:-1,borderWidth:'2px 0 0 2px'}}></span><span style={{...tick,top:-1,right:-1,borderWidth:'2px 2px 0 0'}}></span><span style={{...tick,bottom:-1,left:-1,borderWidth:'0 0 2px 2px'}}></span><span style={{...tick,bottom:-1,right:-1,borderWidth:'0 2px 2px 0'}}></span></>}
    {title&&<div style={{fontFamily:'var(--font-display)',fontSize:11,fontWeight:600,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',color:accent,marginBottom:'var(--sp-3)'}}>{title}</div>}
    {children}
  </div>;
}
