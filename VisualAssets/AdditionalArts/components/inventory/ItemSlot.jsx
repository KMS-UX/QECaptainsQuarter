import React from 'react';
const RC={common:'var(--rarity-common)',uncommon:'var(--rarity-uncommon)',rare:'var(--rarity-rare)',epic:'var(--rarity-epic)',legendary:'var(--rarity-legendary)',mythic:'var(--rarity-mythic)',exotic:'var(--rarity-exotic)'};
const RG={epic:'var(--glow-purple)',legendary:'var(--glow-gold)',mythic:'var(--glow-red)',exotic:'var(--glow-teal)'};
const BADGE={
  new:{txt:'NEW',bg:'var(--atom-gold)',fg:'#1a1204'},
  favorite:{txt:'★',bg:'transparent',fg:'var(--atom-gold)'},
  equipped:{txt:'E',bg:'var(--electric-blue)',fg:'#fff'},
  locked:{txt:'🔒',bg:'transparent',fg:'var(--text-muted)'},
};
export function ItemSlot({rarity='common',icon,image,count,quality,badge,selected=false,size=48,empty=false,onClick,style}){
  const c=RC[rarity]||RC.common; const b=BADGE[badge];
  return <div onClick={onClick} style={{position:'relative',width:size,height:size,flex:'none',cursor:onClick?'pointer':'default',
    background:'var(--surface-inset)',border:`2px solid ${empty?'var(--border-panel)':c}`,borderRadius:'var(--radius-1)',
    boxShadow:selected?'var(--glow-teal)':RG[rarity]&&!empty?RG[rarity]:'none',
    outline:selected?'1px solid var(--teal)':'none',
    display:'flex',alignItems:'center',justifyContent:'center',...style}}>
    {image?<img src={image} alt="" style={{width:'100%',height:'100%',objectFit:'contain',imageRendering:'pixelated'}}/>:!empty&&<span style={{color:c,fontSize:size*.42,lineHeight:1}}>{icon||'◆'}</span>}
    {count!=null&&<span style={{position:'absolute',right:2,bottom:0,fontSize:10,fontWeight:700,color:'var(--fg-1)',textShadow:'0 1px 2px #000'}}>{count}</span>}
    {quality&&<span style={{position:'absolute',left:2,bottom:0,fontSize:9,fontWeight:700,color:'var(--success-green)',textShadow:'0 1px 2px #000'}}>{quality}</span>}
    {b&&(b.bg==='transparent'
      ? <span style={{position:'absolute',top:1,right:2,fontSize:size*.24,lineHeight:1,color:b.fg,textShadow:'0 1px 2px #000'}}>{b.txt}</span>
      : <span style={{position:'absolute',top:-6,left:-4,fontFamily:'var(--font-display)',fontSize:8,fontWeight:800,letterSpacing:'.03em',color:b.fg,background:b.bg,padding:'1px 4px',borderRadius:2,boxShadow:'0 1px 3px #000'}}>{b.txt}</span>)}
  </div>;
}
export function RarityTag({rarity='common',children,style}){
  const c=RC[rarity]||RC.common;
  return <span style={{fontFamily:'var(--font-display)',fontSize:9,fontWeight:700,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',color:c,border:`1px solid ${c}`,padding:'2px 8px',borderRadius:'var(--radius-1)',...style}}>{children||rarity}</span>;
}
