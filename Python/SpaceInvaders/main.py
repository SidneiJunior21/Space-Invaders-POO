import pygame
from sprites import SpaceShip
from controller import EnemyController

pygame.init()
SCREEN_WIDTH = 800
SCREEN_HEIGHT = 600
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
pygame.display.set_caption("Space Invaders")
clock = pygame.time.Clock()
FPS = 60
BLACK = (0, 0, 0)

running = True
while running:
    
    all_sprites = pygame.sprite.Group()
    player = SpaceShip(SCREEN_WIDTH, SCREEN_HEIGHT, all_sprites)
    enemy_controller = EnemyController(SCREEN_WIDTH, SCREEN_HEIGHT)

    all_sprites.add(player)
    all_sprites.add(enemy_controller.enemies)
    
    game_active = True
    
    while game_active:
        clock.tick(FPS)
        
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                game_active = False
                running = False

        all_sprites.update() 
        enemy_controller.update()
        
        all_sprites.add(enemy_controller.projectiles)

        pygame.sprite.groupcollide(player.projectiles, enemy_controller.enemies, True, True)
        player_foi_atingido = pygame.sprite.spritecollide(player, enemy_controller.projectiles, True)
        
        if player_foi_atingido or not enemy_controller.enemies:
            if player_foi_atingido:
                print("Jogador atingido! Reiniciando...")
            else:
                print("Inimigos derrotados! Reiniciando...")
            
            game_active = False
            
        screen.fill(BLACK)
        all_sprites.draw(screen)

        pygame.display.flip()

pygame.quit()