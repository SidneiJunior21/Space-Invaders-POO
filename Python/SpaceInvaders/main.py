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

player = SpaceShip(SCREEN_WIDTH, SCREEN_HEIGHT)
enemy_controller = EnemyController(SCREEN_WIDTH, SCREEN_HEIGHT)

all_sprites = pygame.sprite.Group()
all_sprites.add(player)
all_sprites.add(enemy_controller.enemies)

running = True
game_over = False

while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
            
    if not game_over:
        player.update()
        enemy_controller.update()
        
        all_sprites.add(player.projectiles)
        all_sprites.add(enemy_controller.projectiles)

        pygame.sprite.groupcollide(player.projectiles, enemy_controller.enemies, True, True)

        if pygame.sprite.spritecollide(player, enemy_controller.projectiles, True):
            player.handle_death()
            print("Game Over!")
            game_over = True

        if not enemy_controller.enemies:
            print("Você Venceu!")
            game_over = True
            
    screen.fill(BLACK)
    all_sprites.draw(screen)

    pygame.display.flip()

    clock.tick(FPS)

pygame.quit()