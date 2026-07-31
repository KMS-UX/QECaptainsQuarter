import React from 'react';
const RC={common:'var(--rarity-common)',uncommon:'var(--rarity-uncommon)',rare:'var(--rarity-rare)',epic:'var(--rarity-epic)',legendary:'var(--rarity-legendary)',mythic:'var(--rarity-mythic)',exotic:'var(--rarity-exotic)'};
/** Labeled percentage meter — augmentation/ship attribute bars (Equipment §2, Starship stats). */
export function StatMeter({label,value=0,color='var(--teal)',showValue=true,width,style}){
  const pct=Math.max(0,Math.min(100,value));
  return <div style={{display:'flex',alignItems:'center',gap:'var(--sp-2)',width,...style}}>
    {label&&<span style={{fontFamily:'var(--font-display)',fontSize:9,fontWeight:600,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',color:'var(--text-muted)',width:78,flex:'none'}}>{label}</span>}
    <div style={{flex:1,height:8,background:'var(--bar-track)',border:'1px solid var(--border-panel)',position:'relative'}}>
      <div style={{width:pct+'%',height:'100%',background:color,boxShadow:`0 0 6px ${color}`,transition:'width var(--dur-base) var(--ease-ui)'}}></div>
    </div>
    {showValue&&<span style={{fontFamily:'var(--font-ui)',fontSize:10,color:'var(--text-body)',flex:'none',minWidth:34,textAlign:'right'}}>{pct}%</span>}
  </div>;
}
/** Insertable mod chip / augmentation — rarity-framed icon + name + effect line (Equipment §4). */
export function ModChip({name,effect,rarity='common',icon,image,style}){
  const c=RC[rarity]||RC.common;
  return <div style={{display:'flex',alignItems:'center',gap:'var(--sp-3)',padding:'var(--sp-2) var(--sp-3)',background:'var(--surface-panel)',border:`1px solid var(--border-panel)`,borderLeft:`3px solid ${c}`,borderRadius:'var(--radius-1)',...style}}>
    <span style={{width:28,height:28,flex:'none',display:'inline-flex',alignItems:'center',justifyContent:'center',border:`2px solid ${c}`,borderRadius:4,background:'var(--surface-inset)',color:c,fontSize:14,overflow:'hidden'}}>{image?<img src={image} alt="" style={{width:'100%',height:'100%',objectFit:'contain',imageRendering:'pixelated'}}/>:(icon||'◆')}</span>
    <div style={{minWidth:0}}>
      <div style={{fontFamily:'var(--font-display)',fontSize:11,fontWeight:700,letterSpacing:'.03em',textTransform:'uppercase',color:c}}>{name}</div>
      {effect&&<div style={{fontSize:11,color:'var(--text-muted)',lineHeight:1.35}}>{effect}</div>}
    </div>
  </div>;
}
