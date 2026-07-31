import React from 'react';
const EC={quantium:'var(--elem-quantium)',voidium:'var(--elem-voidium)',neutronite:'var(--elem-neutronite)',singularium:'var(--elem-singularium)',phasium:'var(--elem-phasium)',entropium:'var(--elem-entropium)',aeon:'var(--elem-aeon)'};
/** Quantum element crystal card — faceted gem glyph in element color + rarity/source/usage (Starships & Elements §8). */
export function ElementCard({name,element='quantium',rarity,source,usage,description,width=210,style}){
  const c=EC[element]||EC.quantium;
  return <div style={{width,padding:'var(--sp-3)',background:'var(--surface-panel)',border:'1px solid var(--border-panel)',borderRadius:'var(--radius-1)',boxShadow:'var(--shadow-panel)',display:'flex',gap:'var(--sp-3)',...style}}>
    <svg width="34" height="42" viewBox="0 0 34 42" style={{flex:'none',filter:`drop-shadow(0 0 6px ${c})`}}>
      <polygon points="17,1 33,13 27,41 7,41 1,13" fill={c} opacity="0.28"/>
      <polygon points="17,1 33,13 17,20 1,13" fill={c} opacity="0.7"/>
      <polygon points="1,13 17,20 7,41" fill={c} opacity="0.45"/>
      <polygon points="33,13 17,20 27,41" fill={c} opacity="0.55"/>
    </svg>
    <div style={{minWidth:0,flex:1}}>
      <div style={{fontFamily:'var(--font-display)',fontSize:12,fontWeight:800,letterSpacing:'.04em',textTransform:'uppercase',color:c}}>{name}</div>
      {description&&<div style={{fontSize:10.5,color:'var(--text-muted)',lineHeight:1.35,margin:'3px 0 6px'}}>{description}</div>}
      <div style={{display:'flex',flexDirection:'column',gap:2,fontSize:9,letterSpacing:'.04em'}}>
        {rarity&&<div><span style={{color:'var(--text-faint)',textTransform:'uppercase'}}>Rarity </span><span style={{color:'var(--fg-2)'}}>{rarity}</span></div>}
        {source&&<div><span style={{color:'var(--text-faint)',textTransform:'uppercase'}}>Source </span><span style={{color:'var(--fg-2)'}}>{source}</span></div>}
        {usage&&<div><span style={{color:'var(--text-faint)',textTransform:'uppercase'}}>Usage </span><span style={{color:'var(--fg-2)'}}>{usage}</span></div>}
      </div>
    </div>
  </div>;
}
const HZ={safe:{c:'var(--hazard-safe)',label:'Safe'},caution:{c:'var(--hazard-caution)',label:'Caution'},danger:{c:'var(--hazard-danger)',label:'Danger'},extreme:{c:'var(--hazard-extreme)',label:'Extreme'},unknown:{c:'var(--hazard-unknown)',label:'Unknown'}};
/** Quantum-field hazard level badge — diamond glyph + level label (Starships & Elements §10). */
export function HazardBadge({level='safe',note,size=16,style}){
  const h=HZ[level]||HZ.safe;
  return <span style={{display:'inline-flex',alignItems:'center',gap:7,...style}}>
    <span style={{width:size,height:size,flex:'none',transform:'rotate(45deg)',border:`2px solid ${h.c}`,background:'var(--surface-inset)',boxShadow:`0 0 6px ${h.c}`}}></span>
    <span style={{lineHeight:1.15}}><span style={{fontFamily:'var(--font-display)',fontSize:10,fontWeight:700,letterSpacing:'.06em',textTransform:'uppercase',color:h.c,display:'block'}}>{h.label}</span>
    {note&&<span style={{fontSize:10,color:'var(--text-faint)'}}>{note}</span>}</span>
  </span>;
}
