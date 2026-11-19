package com.example.autosell;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import java.util.*;

public class AutoSellScreen extends Screen {
    private TextFieldWidget search, commandField, delayField;
    private List<Item> filtered=List.of();
    private int scroll=0;
    private static final int COLS=9, ROWS=5, CELL=36;
    public AutoSellScreen(){ super(Text.literal("AutoSell")); }
    @Override protected void init(){ search=new TextFieldWidget(textRenderer,width/2-145,48,290,20,Text.literal("Search items")); search.setPlaceholder(Text.literal("Search item...")); addDrawableChild(search); commandField=new TextFieldWidget(textRenderer,width/2-100,height-82,120,18,Text.literal("Sell command")); commandField.setText(AutoSellConfig.command); addDrawableChild(commandField); delayField=new TextFieldWidget(textRenderer,width/2+35,height-62,45,18,Text.literal("Delay seconds")); delayField.setText(Integer.toString(AutoSellConfig.delaySeconds)); addDrawableChild(delayField); refresh(); }
    private void refresh(){ String q=search==null?"":search.getText().toLowerCase(Locale.ROOT); filtered=AutoSellConfig.items().stream().filter(i->q.isEmpty()||i.getName().getString().toLowerCase(Locale.ROOT).contains(q)||AutoSellConfig.id(i).contains(q)).toList(); scroll=Math.min(scroll,Math.max(0,(filtered.size()-COLS*ROWS+COLS-1)/COLS)); }
    @Override public void render(DrawContext ctx,int mouseX,int mouseY,float delta){ renderBackground(ctx,mouseX,mouseY,delta); int x0=width/2-(COLS*CELL)/2, y0=80; ctx.fill(width/2-190,18,width/2+190,height-22,0xE90D0B13); ctx.fill(width/2-190,18,width/2+190,42,0xFF34203D); ctx.drawCenteredTextWithShadow(textRenderer,"AUTO SELL • ALLOWED ITEMS",width/2,25,0xFFE4C8FF);
        for(int i=0;i<COLS*ROWS;i++){ int index=i+scroll*COLS; if(index>=filtered.size())break; Item item=filtered.get(index); int x=x0+(i%COLS)*CELL,y=y0+(i/COLS)*CELL; boolean chosen=AutoSellConfig.selected.contains(AutoSellConfig.id(item)); ctx.fill(x,y,x+32,y+32,chosen?0xFF285C38:0xFF29252F); ctx.fill(x+1,y+1,x+31,y+31,chosen?0xFF183321:0xFF17151B); ctx.drawItem(new ItemStack(item),x+8,y+8); if(mouseX>=x&&mouseX<x+32&&mouseY>=y&&mouseY<y+32) ctx.drawTooltip(textRenderer,item.getName(),mouseX,mouseY); }
        ctx.drawTextWithShadow(textRenderer,"Selected: "+AutoSellConfig.selected.size()+"   Scroll: "+(scroll+1),x0,y0+ROWS*CELL+8,0xFFB8B0C0);
        ctx.drawTextWithShadow(textRenderer,"Command:",width/2-180,height-78,0xFFFFFFFF); ctx.drawTextWithShadow(textRenderer,"Delay (s):",width/2-180,height-58,0xFFFFFFFF);
        int toggleColor=AutoSellConfig.enabled?0xFF2E7844:0xFF59404B; ctx.fill(width/2+130,18,width/2+190,42,toggleColor); ctx.drawCenteredTextWithShadow(textRenderer,AutoSellConfig.enabled?"SELL ON":"SELL OFF",width/2+160,25,0xFFFFFFFF);
        ctx.drawTextWithShadow(textRenderer,"[Click] toggle item  •  [Up/Down] scroll  •  Right Shift close",width/2-180,height-42,0xFFAAAAAA);
        super.render(ctx,mouseX,mouseY,delta);
    }
    @Override public boolean mouseClicked(double mouseX,double mouseY,int button){ if(search.mouseClicked(mouseX,mouseY,button)||commandField.mouseClicked(mouseX,mouseY,button)||delayField.mouseClicked(mouseX,mouseY,button))return true; if(mouseX>=width/2+130&&mouseX<=width/2+190&&mouseY>=18&&mouseY<=42){AutoSellConfig.enabled=!AutoSellConfig.enabled;AutoSellConfig.save();return true;} int x0=width/2-(COLS*CELL)/2,y0=80; for(int i=0;i<COLS*ROWS;i++){int index=i+scroll*COLS;if(index>=filtered.size())break; int x=x0+(i%COLS)*CELL,y=y0+(i/COLS)*CELL;if(mouseX>=x&&mouseX<x+32&&mouseY>=y&&mouseY<y+32){String id=AutoSellConfig.id(filtered.get(index));if(!AutoSellConfig.selected.add(id))AutoSellConfig.selected.remove(id);AutoSellConfig.save();return true;}} return super.mouseClicked(mouseX,mouseY,button); }
    @Override public boolean keyPressed(int keyCode,int scanCode,int modifiers){ if(search.keyPressed(keyCode,scanCode,modifiers)){refresh();return true;} if(commandField.keyPressed(keyCode,scanCode,modifiers)||delayField.keyPressed(keyCode,scanCode,modifiers))return true; if(keyCode==256){close();return true;} if(keyCode==265){scroll=Math.max(0,scroll-1);return true;} if(keyCode==264){scroll=Math.min(Math.max(0,(filtered.size()-COLS*ROWS+COLS-1)/COLS),scroll+1);return true;} return super.keyPressed(keyCode,scanCode,modifiers); }
    @Override public boolean charTyped(char chr,int modifiers){if(search.charTyped(chr,modifiers)){refresh();return true;} if(commandField.charTyped(chr,modifiers)||delayField.charTyped(chr,modifiers))return true; return super.charTyped(chr,modifiers);}
    @Override public void close(){AutoSellConfig.command=commandField.getText().trim().isEmpty()?"/sell":commandField.getText().trim(); try{AutoSellConfig.delaySeconds=Math.max(2,Math.min(300,Integer.parseInt(delayField.getText().trim())));}catch(Exception ignored){} AutoSellConfig.save();MinecraftClient.getInstance().setScreen(null);}
}
