import React from 'react';
const RC={common:'var(--rarity-common)',uncommon:'var(--rarity-uncommon)',rare:'var(--rarity-rare)',epic:'var(--rarity-epic)',legendary:'var(--rarity-legendary)',mythic:'var(--rarity-mythic)',exotic:'var(--rarity-exotic)'};
function Pill({glyph,color,value,label}){
  return <div style={{display:'flex',alignItems:'center',gap:7}}>
    <span style={{width:22,height:22,flex:'none',display:'inline-flex',alignItems:'center',justifyContent:'center',color:color,border:`1.5px solid ${color}`,borderRadius:4,fontSize:11,fontWeight:700}}>{glyph}</span>
    <div style={{lineHeight:1.15}}><div style={{fontFamily:'var(--font-ui)',fontSize:14,fontWeight:700,color:'var(--fg-1)'}}>{value}</div>
    <div style={{fontSize:9,letterSpacing:'.08em',textTransform:'uppercase',color:'var(--text-faint)'}}>{label}</div></div>
  </div>;
}
export function QuestRewards({xp,reputation,currency,items=[],choice,style}){
  return <div style={{display:'flex',flexDirection:'column',gap:'var(--sp-4)',...style}}>
    <div style={{display:'flex',gap:'var(--sp-5)',flexWrap:'wrap'}}>
      {xp!=null&&<Pill glyph="XP" color="var(--electric-blue)" value={xp} label="Experience"/>}
      {reputation!=null&&<Pill glyph="◆" color="var(--quantum-purple)" value={reputation} label="Reputation"/>}
      {currency!=null&&<Pill glyph="◉" color="var(--atom-gold)" value={currency} label="Credits"/>}
    </div>
    {items.length>0&&<div><div style={{fontSize:9,letterSpacing:'.1em',textTransform:'uppercase',color:'var(--text-faint)',marginBottom:6}}>Items</div>
      <div style={{display:'flex',gap:8}}>{items.map((it,i)=><span key={i} title={it.name} style={{width:36,height:36,borderRadius:4,border:`2px solid ${RC[it.rarity]||RC.common}`,background:'var(--surface-inset)',display:'inline-flex',alignItems:'center',justifyContent:'center',overflow:'hidden'}}>{it.image?<img src={it.image} alt="" style={{width:'100%',height:'100%',objectFit:'contain',imageRendering:'pixelated'}}/>:<span style={{color:RC[it.rarity]||RC.common}}>◆</span>}</span>)}</div></div>}
    {choice&&choice.length>0&&<div><div style={{fontSize:9,letterSpacing:'.1em',textTransform:'uppercase',color:'var(--atom-gold)',marginBottom:6}}>Choose One</div>
      <div style={{display:'flex',gap:10,alignItems:'center'}}>{choice.map((it,i)=><React.Fragment key={i}>{i>0&&<span style={{fontSize:10,color:'var(--text-faint)'}}>OR</span>}<span title={it.name} style={{width:40,height:40,borderRadius:4,border:`2px solid ${RC[it.rarity]||RC.legendary}`,background:'var(--surface-inset)',boxShadow:'var(--glow-gold)',display:'inline-flex',alignItems:'center',justifyContent:'center',overflow:'hidden'}}>{it.image?<img src={it.image} alt="" style={{width:'100%',height:'100%',objectFit:'contain',imageRendering:'pixelated'}}/>:<span style={{color:RC[it.rarity]||RC.legendary}}>◆</span>}</span></React.Fragment>)}</div></div>}
  </div>;
}
