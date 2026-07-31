import React from 'react';
const MC={player:'var(--marker-player)',ally:'var(--marker-ally)',enemy:'var(--marker-enemy)',objective:'var(--marker-objective)',poi:'var(--marker-poi)'};
function Blip({b,size}){
  const c=MC[b.kind]||MC.poi, x=b.x*size, y=b.y*size;
  if(b.kind==='player') return <polygon points={`${x},${y-5} ${x+4},${y+4} ${x-4},${y+4}`} fill={c} stroke="#000" strokeWidth="0.5"/>;
  if(b.kind==='objective') return <g><circle cx={x} cy={y} r="4" fill="none" stroke={c} strokeWidth="1.5"/><circle cx={x} cy={y} r="1.5" fill={c}/></g>;
  return <circle cx={x} cy={y} r={b.kind==='enemy'?3:2.5} fill={c} stroke="#000" strokeWidth="0.4"/>;
}
export function Minimap({shape='circular',size=140,blips=[],fog=false,heading=0,label,style}){
  const clip=shape==='circular'?'circle(50%)':'none';
  const dots=blips.length?blips:[{kind:'player',x:0.5,y:0.5},{kind:'ally',x:0.62,y:0.4},{kind:'enemy',x:0.35,y:0.6},{kind:'objective',x:0.7,y:0.7},{kind:'poi',x:0.28,y:0.33}];
  return <div style={{display:'inline-flex',flexDirection:'column',alignItems:'center',gap:'var(--sp-2)',...style}}>
    <div style={{position:'relative',width:size,height:size}}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} style={{display:'block',clipPath:clip,background:'var(--map-terrain)',border:`2px solid var(--border-bright)`,borderRadius:shape==='circular'?'50%':'var(--radius-1)',boxShadow:'var(--shadow-panel)'}}>
        <defs><radialGradient id="fg" cx="50%" cy="50%" r="55%"><stop offset="60%" stopColor="transparent"/><stop offset="100%" stopColor="var(--map-fog)"/></radialGradient></defs>
        <g opacity="0.5" stroke="var(--map-terrain-hi)" strokeWidth="1">
          {[...Array(5)].map((_,i)=><line key={'h'+i} x1="0" y1={size/5*i} x2={size} y2={size/5*i}/>)}
          {[...Array(5)].map((_,i)=><line key={'v'+i} x1={size/5*i} y1="0" x2={size/5*i} y2={size}/>)}
        </g>
        <path d={`M${size*0.15},${size*0.7} Q${size*0.4},${size*0.4} ${size*0.85},${size*0.55}`} fill="none" stroke="var(--map-terrain-hi)" strokeWidth="6" opacity="0.7"/>
        {dots.map((b,i)=><Blip key={i} b={b} size={size}/>)}
        {fog&&<rect width={size} height={size} fill="url(#fg)"/>}
        {shape==='circular'&&<circle cx={size/2} cy={size/2} r={size/2-3} fill="none" stroke="var(--border-accent)" strokeWidth="1" opacity="0.4"/>}
      </svg>
      {shape==='circular'&&<span style={{position:'absolute',top:2,left:'50%',transform:'translateX(-50%)',fontFamily:'var(--font-display)',fontSize:9,fontWeight:700,color:'var(--text-section)',textShadow:'0 1px 2px #000'}}>N</span>}
    </div>
    {label&&<span style={{fontFamily:'var(--font-display)',fontSize:9,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',color:'var(--text-muted)'}}>{label}</span>}
  </div>;
}
